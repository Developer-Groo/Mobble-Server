package com.mobble.mobbleserver.account.auth.dto.request;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;

public record SocialLoginRequestDto(
        String accessToken,
        SocialProvider socialProvider
) {
}
