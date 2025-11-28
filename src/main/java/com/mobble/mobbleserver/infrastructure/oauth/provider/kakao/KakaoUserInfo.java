package com.mobble.mobbleserver.infrastructure.oauth.provider.kakao;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.error.OAuthBusinessError;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.infrastructure.oauth.common.OAuth2UserInfo;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        if (attributes == null || attributes.get("id") == null || attributes.get("kakao_account") == null) throw new BusinessException(OAuthBusinessError.INVALID_USER_INFO);
        this.attributes = attributes;
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.KAKAO;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null || kakaoAccount.get("email") == null) throw new BusinessException(OAuthBusinessError.INVALID_USER_INFO);

        return kakaoAccount.get("email").toString();
    }
}
