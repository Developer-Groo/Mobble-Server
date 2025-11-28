package com.mobble.mobbleserver.infrastructure.oauth.provider.apple;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.error.OAuthBusinessError;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.infrastructure.oauth.common.OAuth2UserInfo;


public class AppleUserInfo implements OAuth2UserInfo {

    private final DecodedJWT jwt;

    public AppleUserInfo(DecodedJWT jwt) {
        if (jwt == null || jwt.getSubject() == null) throw new BusinessException(OAuthBusinessError.INVALID_USER_INFO);
        this.jwt = jwt;
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.APPLE;
    }

    @Override
    public String getProviderId() {
        return jwt.getSubject();
    }

    @Override
    public String getEmail() {
        // null 체크X (첫 로그인 시만 email 제공 그 이후는 제공 안 함)
        return jwt.getClaim("email").asString();
    }
}
