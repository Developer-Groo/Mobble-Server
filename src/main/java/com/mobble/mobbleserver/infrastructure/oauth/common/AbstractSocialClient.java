package com.mobble.mobbleserver.infrastructure.oauth.common;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.error.OAuthBusinessError;
import com.mobble.mobbleserver.application.exception.BusinessException;
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
                    throw new BusinessException(OAuthBusinessError.INVALID_ACCESS_TOKEN);
                })
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public abstract SocialProvider getProvider();

    protected abstract String getUserInfoUri();

    protected abstract OAuth2UserInfo parseUserInfo(Map<String, Object> attributes);
}
