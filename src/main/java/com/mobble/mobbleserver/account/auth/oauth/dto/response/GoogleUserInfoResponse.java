package com.mobble.mobbleserver.account.auth.oauth.dto.response;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;

import java.util.Map;

public class GoogleUserInfoResponse implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    private final SocialProvider socialProvider = SocialProvider.GOOGLE;

    public GoogleUserInfoResponse(Map<String, Object> attributes) {
        if (attributes == null || attributes.get("sub") == null) throw new DomainException(OAuthErrorCode.NO_USER_INFO);
        this.attributes = attributes;
    }

    @Override
    public SocialProvider getProvider() {
        return socialProvider;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        if (attributes.get("email") == null) throw new DomainException(OAuthErrorCode.NO_USER_INFO);

        return attributes.get("email").toString();
    }
}
