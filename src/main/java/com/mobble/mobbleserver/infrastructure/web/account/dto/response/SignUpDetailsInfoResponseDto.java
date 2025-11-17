package com.mobble.mobbleserver.infrastructure.web.account.dto.response;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;

public record SignUpDetailsInfoResponseDto(
        String email,
        SocialProvider socialProvider,
        String socialId
) {

    public static SignUpDetailsInfoResponseDto toDto(SocialUserInfo userInfo) {
        return new SignUpDetailsInfoResponseDto(
                userInfo.email(),
                userInfo.socialProvider(),
                userInfo.socialId()
        );
    }
}
