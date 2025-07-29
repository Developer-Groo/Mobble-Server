package com.mobble.mobbleserver.account.oauth2.verifier;

import com.mobble.mobbleserver.account.oauth2.dto.response.KakaoUserInfoResponse;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth2.OAuth2ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoTokenVerifier implements SocialVerifier {

    private final RestClient kakaoRestClient;

    @Override
    public SocialUserInfo verify(String accessToken) {
        Map<String, Object> response = kakaoRestClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(Map.class);

        if (response == null || response.get("id") == null || response.get("kakao_account") == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }

        KakaoUserInfoResponse userInfo = new KakaoUserInfoResponse(response);

        return new SocialUserInfo(
                userInfo.getName(),
                userInfo.getEmail(),
                userInfo.getProvider(),
                userInfo.getProviderId()
        );
    }
}
