package com.mobble.mobbleserver.application.account.command;

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
