package com.mobble.mobbleserver.infrastructure.oauth.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;
import com.mobble.mobbleserver.infrastructure.oauth.provider.userInfoResult.AppleUserInfoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AppleClient {

    private final RestClient appleRestClient;

    public SocialUserInfo fetchUserInfo(String idToken) {
        try {
            // 전달받은 id_token 디코드 (검증 전 상태)
            DecodedJWT jwt = JWT.decode(idToken);

            // Apple의 공개키(JWK Set)를 가져와서 key ID(kid)가 일치하는 키를 찾음
            JsonNode jwkResponse = appleRestClient.get()
                    .uri("/auth/keys") // baseUrl + "/auth/keys"
                    .retrieve()
                    .body(JsonNode.class);

            if (jwkResponse == null || jwkResponse.get("keys") == null) {
                throw new DomainException(OAuthErrorCode.FAILED_TO_REQUEST_USER_INFO);
            }

            JsonNode keys = jwkResponse.get("keys");
            JsonNode matchingKey = findMatchingKey(keys, jwt.getKeyId());

            // JWK를 기반으로 RSA 공개키 생성
            RSAPublicKey publicKey = buildPublicKey(matchingKey);

            // Apple이 서명한 id_token이 이 공개키로 유효한지 검증
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            algorithm.verify(jwt); // 서명 검증 실패 시 예외 발생

            AppleUserInfoResult userInfo = new AppleUserInfoResult(jwt);

            return new SocialUserInfo(
                    userInfo.getEmail(),
                    userInfo.getProvider(),
                    userInfo.getProviderId()
            );
        } catch (DomainException e) {
            // 우리가 명시적으로 던진 예외는 그대로 전달
            throw e;
        } catch (Exception e) {
            // 통신 실패, 키 생성 실패, 검증 실패 등 나머지 예외들
            throw new DomainException(OAuthErrorCode.FAILED_TO_REQUEST_USER_INFO);
        }
    }

    /**
     * Apple의 JWK Set에서 JWT의 kid와 일치하는 키를 찾아 반환
     */
    private JsonNode findMatchingKey(JsonNode keys, String kid) {
        for (JsonNode key : keys) {
            if (key.get("kid").asText().equals(kid)) {
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
