package com.mobble.mobbleserver.application.account.command;

public record SocialUserInfo(
        String email,
        SocialProvider socialProvider,
        String socialId
) {
}
