package com.mobble.mobbleserver.infrastructure.oauth.provider;

import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;
import com.mobble.mobbleserver.infrastructure.oauth.provider.userInfoResult.KakaoUserInfoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoClient {

    private final RestClient kakaoRestClient;

    public SocialUserInfo fetchUserInfo(String accessToken) {
        try {
            Map<String, Object> response = kakaoRestClient.get()
                    .uri("/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        throw new DomainException(OAuthErrorCode.INVALID_ACCESS_TOKEN);
                    })
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            KakaoUserInfoResult userInfo = new KakaoUserInfoResult(response);

            return new SocialUserInfo(
                    userInfo.getEmail(),
                    userInfo.getProvider(),
                    userInfo.getProviderId()
            );
        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            throw new DomainException(OAuthErrorCode.FAILED_TO_REQUEST_USER_INFO);
        }
    }
}
