package com.mobble.mobbleserver.infrastructure.oauth.provider.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;
import com.mobble.mobbleserver.infrastructure.oauth.common.AbstractSocialClient;
import com.mobble.mobbleserver.infrastructure.oauth.common.OAuth2UserInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Iterator;

@Component
public class AppleClient extends AbstractSocialClient {

    public AppleClient(@Qualifier("appleRestClient") RestClient restClient) {
        super(restClient);
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.APPLE;
    }

    @Override
    public OAuth2UserInfo fetchUserInfo(String idToken) {
        try {
            // 전달받은 id_token 디코드 (검증 전 상태)
            DecodedJWT jwt = JWT.decode(idToken);
            String kid = jwt.getKeyId();
            String alg = jwt.getAlgorithm();

            // Apple 공개키(JWK Set) 조회
            JsonNode jwkResponse = restClient.get()
                    .uri("/auth/keys")
                    .retrieve()
                    .body(JsonNode.class);

            if (jwkResponse == null || jwkResponse.get("keys") == null) {
                throw new DomainException(OAuthErrorCode.FAILED_TO_REQUEST_USER_INFO);
            }

            // Apple 공개키(JWK Set)에서 key ID(kid)가 일치하는 키를 찾아 반환
            JsonNode keys = jwkResponse.get("keys");
            JsonNode matchingKey = findMatchingKey(keys, kid, alg);

            // JWK 기반 RSA 공개키 생성
            RSAPublicKey publicKey = buildPublicKey(matchingKey);

            // 공개키로 id_token 서명 검증
            DecodedJWT verifiedJwt = JWT.require(Algorithm.RSA256(publicKey, null))
                    .build()
                    .verify(idToken);

            return new AppleUserInfo(verifiedJwt);

        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            throw new DomainException(OAuthErrorCode.FAILED_TO_REQUEST_USER_INFO);
        }
    }

    @Override
    protected String getUserInfoUri() {
        throw new UnsupportedOperationException("Apple doesn't use accessToken-based userinfo.");
    }

    @Override
    protected OAuth2UserInfo parseUserInfo(java.util.Map<String, Object> attributes) {
        throw new UnsupportedOperationException("Apple doesn't use attributes-based userinfo.");
    }

    private JsonNode findMatchingKey(JsonNode keys, String kid, String alg) {
        if (keys == null || !keys.isArray()) {
            throw new DomainException(OAuthErrorCode.FAILED_TO_REQUEST_USER_INFO);
        }

        Iterator<JsonNode> it = keys.elements();
        while (it.hasNext()) {
            JsonNode key = it.next();
            if (kid.equals(key.get("kid").asText()) && alg.equals(key.get("alg").asText())) {
                return key;
            }
        }
        throw new DomainException(OAuthErrorCode.INVALID_ACCESS_TOKEN);
    }

    /**
     * Apple의 JWK(Json Web Key)에서 RSA 공개키 객체 생성
     * - n: modulus (base64url)
     * - e: exponent (base64url)
     */
    private RSAPublicKey buildPublicKey(JsonNode key) throws Exception {
        String n = key.get("n").asText();
        String e = key.get("e").asText();

        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new RSAPublicKeySpec(modulus, exponent));
    }
}
