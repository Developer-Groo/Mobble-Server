package com.mobble.mobbleserver.account.auth.dto.response;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;

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
