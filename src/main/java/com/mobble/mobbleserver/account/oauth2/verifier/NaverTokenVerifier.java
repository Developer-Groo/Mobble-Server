package com.mobble.mobbleserver.account.oauth2.verifier;

import com.mobble.mobbleserver.account.oauth2.dto.response.NaverUserInfoResponse;
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
public class NaverTokenVerifier implements SocialVerifier {

    private final RestClient naverRestClient;

    @Override
    public SocialUserInfo verify(String accessToken) {
        Map<String, Object> response;

        try {
            response = naverRestClient.get()
                    .uri("/v1/nid/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        throw new DomainException(OAuth2ErrorCode.INVALID_ACCESS_TOKEN);
                    })
                    .body(Map.class);
        } catch (Exception e) {
            throw new DomainException(OAuth2ErrorCode.FAILED_TO_REQUEST_USER_INFO);
        }

        NaverUserInfoResponse userInfo = new NaverUserInfoResponse(response);

        return new SocialUserInfo(
                userInfo.getName(),
                userInfo.getEmail(),
                userInfo.getProvider(),
                userInfo.getProviderId()
        );
    }
}
