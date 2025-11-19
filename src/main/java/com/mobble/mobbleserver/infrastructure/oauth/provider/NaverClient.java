package com.mobble.mobbleserver.infrastructure.oauth.provider;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.infrastructure.oauth.provider.userInfoResult.KakaoUserInfoResult;
import com.mobble.mobbleserver.infrastructure.oauth.provider.userInfoResult.OAuth2UserInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class NaverClient extends AbstractSocialClient {

    public NaverClient(@Qualifier("naverRestClient") RestClient restClient) {
        super(restClient);
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.NAVER;
    }

    @Override
    protected String getUserInfoUri() {
        return "/v1/nid/me";
    }

    @Override
    protected OAuth2UserInfo parseUserInfo(Map<String, Object> attributes) {
        return new KakaoUserInfoResult(attributes);
    }
}
