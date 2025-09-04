package com.mobble.mobbleserver.account.auth.oauth.service;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SocialProvider {
    KAKAO,
    NAVER,
    GOOGLE,
    APPLE;

    @JsonCreator
    public static SocialProvider fromString(String socialProvider) {
        return SocialProvider.valueOf(socialProvider.toUpperCase());
    }
}
