package com.mobble.mobbleserver.infrastructure.web.account.dto.response;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;

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
