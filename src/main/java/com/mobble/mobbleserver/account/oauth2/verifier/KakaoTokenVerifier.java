package com.mobble.mobbleserver.account.oauth2.verifier;

import com.mobble.mobbleserver.account.oauth2.dto.response.KakaoUserInfoResponse;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth2.OAuth2ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoTokenVerifier implements SocialVerifier {

    private final RestClient kakaoRestClient;

    @Override
    public SocialUserInfo verify(String accessToken) {
        Map<String, Object> response;

        try {
            response = kakaoRestClient.get()
                    .uri("/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (httpRequest, httpResponse) -> {
                        throw new DomainException(OAuth2ErrorCode.INVALID_ACCESS_TOKEN);
                    })
                    .body(Map.class);
        } catch (Exception e) {
            throw new DomainException(OAuth2ErrorCode.FAILED_TO_REQUEST_USER_INFO);
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
