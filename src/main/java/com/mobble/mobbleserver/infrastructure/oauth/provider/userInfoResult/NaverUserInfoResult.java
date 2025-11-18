package com.mobble.mobbleserver.infrastructure.oauth.provider.userInfoResult;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;

import java.util.Map;

public class NaverUserInfoResult implements OAuth2UserInfo {

    private final Map<String, Object> response;

    private final SocialProvider socialProvider = SocialProvider.NAVER;

    public NaverUserInfoResult(Map<String, Object> rawResponse) {
        if (rawResponse == null || rawResponse.get("response") == null) throw new DomainException(OAuthErrorCode.NO_USER_INFO);
        this.response = (Map<String, Object>) rawResponse.get("response");
    }

    @Override
    public SocialProvider getProvider() {
        return socialProvider;
    }

    @Override
    public String getProviderId() {
        if (response.get("id") == null) throw new DomainException(OAuthErrorCode.NO_USER_INFO);

        return response.get("id").toString();
    }

    @Override
    public String getEmail() {
        if (response.get("email") == null) throw new DomainException(OAuthErrorCode.NO_USER_INFO);

        return response.get("email").toString();
    }
}
