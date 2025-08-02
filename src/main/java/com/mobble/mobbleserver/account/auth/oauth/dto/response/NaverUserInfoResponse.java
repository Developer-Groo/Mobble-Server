package com.mobble.mobbleserver.account.auth.oauth.dto.response;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth2.OAuth2ErrorCode;

import java.util.Map;

public class NaverUserInfoResponse implements OAuth2UserInfo {

    private final Map<String, Object> response;

    private final SocialProvider socialProvider = SocialProvider.NAVER;

    public NaverUserInfoResponse(Map<String, Object> rawResponse) {
        this.response = (Map<String, Object>) rawResponse.get("response");

        if (this.response == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }
    }

    @Override
    public SocialProvider getProvider() {
        return socialProvider;
    }

    @Override
    public String getProviderId() {
        if (response.get("id") == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }
        return response.get("id").toString();
    }

    @Override
    public String getName() {
        if (response.get("name") == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }
        return response.get("name").toString();
    }

    @Override
    public String getEmail() {
        if (response.get("email") == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }
        return response.get("email").toString();
    }
}
