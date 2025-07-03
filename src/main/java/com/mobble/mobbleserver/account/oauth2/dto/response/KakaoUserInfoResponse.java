package com.mobble.mobbleserver.account.oauth2.dto.response;

import com.mobble.mobbleserver.account.oauth2.provider.SocialProvider;

import java.util.Map;

public class KakaoUserInfoResponse implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final SocialProvider socialProvider = SocialProvider.KAKAO;

    public KakaoUserInfoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public SocialProvider getProvider() {
        return socialProvider;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return ((Map<?, ?>) attributes.get("properties")).get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return ((Map<?, ?>) attributes.get("kakao_account")).get("email").toString();
    }
}
