package com.mobble.mobbleserver.account.auth.oauth.dto.response;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth2.OAuth2ErrorCode;

import java.util.Map;

public class KakaoUserInfoResponse implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    private final SocialProvider socialProvider = SocialProvider.KAKAO;

    public KakaoUserInfoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
        if (attributes == null || attributes.get("id") == null || attributes.get("kakao_account") == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }
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
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null || kakaoAccount.get("name") == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }

        return kakaoAccount.get("name").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null || kakaoAccount.get("email") == null) {
            throw new DomainException(OAuth2ErrorCode.NO_USER_INFO);
        }

        return kakaoAccount.get("email").toString();
    }
}
