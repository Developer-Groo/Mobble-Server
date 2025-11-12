package com.mobble.mobbleserver.infrastructure.web.account.dto.request;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;

public record SocialLoginRequestDto(String accessToken, SocialProvider socialProvider) {
}
