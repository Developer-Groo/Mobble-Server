package com.mobble.mobbleserver.infrastructure.oauth.provider;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.infrastructure.oauth.provider.userInfoResult.GoogleUserInfoResult;
import com.mobble.mobbleserver.infrastructure.oauth.provider.userInfoResult.OAuth2UserInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class GoogleClient extends AbstractSocialClient {

    public GoogleClient(@Qualifier("googleRestClient") RestClient restClient) {
        super(restClient);
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.GOOGLE;
    }

    @Override
    protected String getUserInfoUri() {
        return "/oauth2/v3/userinfo";
    }

    @Override
    protected OAuth2UserInfo parseUserInfo(Map<String, Object> attributes) {
        return new GoogleUserInfoResult(attributes);
    }
}
