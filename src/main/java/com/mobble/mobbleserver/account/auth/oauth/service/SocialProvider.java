package com.mobble.mobbleserver.account.auth.oauth.service;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SocialProvider {
    KAKAO,
    NAVER,
    GOOGLE;

    @JsonCreator
    public static SocialProvider fromString(String socialProvider) {
        return SocialProvider.valueOf(socialProvider.toUpperCase());
    }
}
