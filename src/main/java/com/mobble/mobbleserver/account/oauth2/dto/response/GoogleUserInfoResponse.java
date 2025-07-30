package com.mobble.mobbleserver.account.oauth2.dto.response;

import com.mobble.mobbleserver.account.oauth2.service.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth2.OAuth2ErrorCode;

import java.util.Map;

public class GoogleUserInfoResponse implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    private final SocialProvider socialProvider = SocialProvider.GOOGLE;

    public GoogleUserInfoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
        if (attributes == null || attributes.get("sub") == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }
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
    public String getName() {
        if (attributes.get("name") == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }

        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        if (attributes.get("email") == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }

        return attributes.get("email").toString();
    }
}
