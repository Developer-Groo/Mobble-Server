package com.mobble.mobbleserver.account.oauth2.dto.response;

import com.mobble.mobbleserver.account.oauth2.service.SocialProvider;

import java.util.Map;

public class GoogleUserInfoResponse implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    private final SocialProvider socialProvider = SocialProvider.GOOGLE;

    public GoogleUserInfoResponse(Map<String, Object> attributes) {
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
        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }
}
