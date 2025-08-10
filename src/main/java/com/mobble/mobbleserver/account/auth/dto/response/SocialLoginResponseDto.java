package com.mobble.mobbleserver.account.auth.dto.response;

public record SocialLoginResponseDto(String accessToken, boolean isNewMember) {

    public static SocialLoginResponseDto existMember(String accessToken) {
        return new SocialLoginResponseDto(accessToken, false);
    }

    public static SocialLoginResponseDto newMember(String signupToken) {
        return new SocialLoginResponseDto(signupToken, true);
    }
}

