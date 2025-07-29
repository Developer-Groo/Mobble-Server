package com.mobble.mobbleserver.account.auth.dto.request;

import com.mobble.mobbleserver.account.oauth2.service.SocialProvider;

public record SocialLoginRequestDto(
        String accessToken,
        SocialProvider socialProvider
) {
}
