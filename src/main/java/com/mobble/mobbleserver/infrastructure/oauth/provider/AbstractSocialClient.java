package com.mobble.mobbleserver.infrastructure.oauth.provider;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;
import com.mobble.mobbleserver.infrastructure.oauth.provider.userInfoResult.OAuth2UserInfo;
import org.apache.http.HttpHeaders;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.util.Map;

public abstract class AbstractSocialClient {

    protected final RestClient restClient;

    protected AbstractSocialClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public OAuth2UserInfo fetchUserInfo(String token) {
        Map<String, Object> attributes = requestUserInfo(token);

        return parseUserInfo(attributes);
    }

    protected Map<String, Object> requestUserInfo(String accessToken) {
        return restClient.get()
                .uri(getUserInfoUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new DomainException(OAuthErrorCode.INVALID_ACCESS_TOKEN);
                })
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }

    public abstract SocialProvider getProvider();

    protected abstract String getUserInfoUri();

    protected abstract OAuth2UserInfo parseUserInfo(Map<String, Object> attributes);
}
