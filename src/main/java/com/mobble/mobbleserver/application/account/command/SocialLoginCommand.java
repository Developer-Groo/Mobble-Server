package com.mobble.mobbleserver.application.account.command;

public record SocialLoginCommand(String accessToken, SocialProvider socialProvider) {

    public static SocialLoginCommand create(String accessToken, SocialProvider socialProvider) {
        return new SocialLoginCommand(accessToken, socialProvider);
    }
}
