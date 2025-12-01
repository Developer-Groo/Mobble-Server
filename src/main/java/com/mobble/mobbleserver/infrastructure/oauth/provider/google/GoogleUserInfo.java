package com.mobble.mobbleserver.infrastructure.oauth.provider.google;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.error.OAuthBusinessError;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.infrastructure.oauth.common.OAuth2UserInfo;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        if (attributes == null || attributes.get("sub") == null) throw new BusinessException(OAuthBusinessError.INVALID_USER_INFO);
        this.attributes = attributes;
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        if (attributes.get("email") == null) throw new BusinessException(OAuthBusinessError.INVALID_USER_INFO);

        return attributes.get("email").toString();
    }
}
