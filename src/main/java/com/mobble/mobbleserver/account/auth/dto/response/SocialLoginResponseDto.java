package com.mobble.mobbleserver.account.auth.dto.response;

public record SocialLoginResponseDto(
        String accessToken
) {

    public static SocialLoginResponseDto toDto(String accessToken) {
        return new SocialLoginResponseDto(accessToken);
    }
}
