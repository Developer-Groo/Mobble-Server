package com.mobble.mobbleserver.account.auth.dto.response;

public record TokenResponseDto(String accessToken) {

    public static TokenResponseDto toDto(String accessToken) {
        return new TokenResponseDto(accessToken);
    }
}
