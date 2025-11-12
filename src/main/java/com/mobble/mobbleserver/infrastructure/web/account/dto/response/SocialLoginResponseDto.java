package com.mobble.mobbleserver.infrastructure.web.account.dto.response;

public record SocialLoginResponseDto(String jwtToken, boolean isNewMember) {

    public static SocialLoginResponseDto existMember(String jwtToken) {
        return new SocialLoginResponseDto(jwtToken, false);
    }

    public static SocialLoginResponseDto newMember(String signupToken) {
        return new SocialLoginResponseDto(signupToken, true);
    }
}

