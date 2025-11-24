package com.mobble.mobbleserver.infrastructure.oauth.provider.kakao;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.infrastructure.oauth.common.AbstractSocialClient;
import com.mobble.mobbleserver.infrastructure.oauth.common.OAuth2UserInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class KakaoClient extends AbstractSocialClient {

    public KakaoClient(@Qualifier("kakaoRestClient") RestClient restClient) {
        super(restClient);
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.KAKAO;
    }

    @Override
    protected String getUserInfoUri() {
        return "/v2/user/me";
    }

    @Override
    protected OAuth2UserInfo parseUserInfo(Map<String, Object> attributes) {
        return new KakaoUserInfo(attributes);
    }
}
