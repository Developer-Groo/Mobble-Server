package com.mobble.mobbleserver.account.auth.oauth.dto.response;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;


public class AppleUserInfoResponse implements OAuth2UserInfo {

    private final DecodedJWT jwt;

    private final SocialProvider socialProvider = SocialProvider.APPLE;

    public AppleUserInfoResponse(DecodedJWT jwt) {
        if (jwt == null || jwt.getSubject() == null) throw new DomainException(OAuthErrorCode.NO_USER_INFO);
        this.jwt = jwt;
    }

    @Override
    public SocialProvider getProvider() {
        return socialProvider;
    }

    @Override
    public String getProviderId() {
        return jwt.getSubject();
    }

    @Override
    public String getName() {
        // Apple은 이름을 제공하지 않으므로 null 반환 (본인인증으로 대체)
        return null;
    }

    @Override
    public String getEmail() {
        // null 체크X (첫 로그인 시만 email 제공 그 이후는 제공 안 함)
        return jwt.getClaim("email").asString();
    }
}
