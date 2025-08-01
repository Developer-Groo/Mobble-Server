package com.mobble.mobbleserver.account.auth.dto.response;

import com.mobble.mobbleserver.account.oauth2.service.SocialProvider;
import com.mobble.mobbleserver.account.oauth2.verifier.SocialUserInfo;

public record SignUpDetailsInfoResponseDto(
        String name,
        String email,
        SocialProvider socialProvider,
        String socialId
) {

    public static SignUpDetailsInfoResponseDto toDto(SocialUserInfo userInfo) {
        return new SignUpDetailsInfoResponseDto(
                userInfo.name(),
                userInfo.email(),
                userInfo.socialProvider(),
                userInfo.socialId()
        );
    }
}
