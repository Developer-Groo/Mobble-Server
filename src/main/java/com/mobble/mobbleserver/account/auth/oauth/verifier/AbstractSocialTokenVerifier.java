package com.mobble.mobbleserver.account.auth.oauth.verifier;

import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractSocialTokenVerifier implements SocialVerifier {

    private final RestClient restClient;

    protected abstract String getUserInfoUri();

    protected abstract SocialUserInfo parseUserInfo(Map<String, Object> attributes);

    @Override
    public SocialUserInfo verify(String accessToken) {
        Map<String, Object> response;
        try {
            response = restClient.get()
                    .uri(getUserInfoUri())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        throw new DomainException(OAuthErrorCode.INVALID_ACCESS_TOKEN);
                    })
                    .body(Map.class);
        } catch (Exception e) {
            throw new DomainException(OAuthErrorCode.FAILED_TO_REQUEST_USER_INFO);
        }
        
        return parseUserInfo(response);
    }
}
