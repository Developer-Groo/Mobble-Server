package com.mobble.mobbleserver.account.auth.util;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {

    public static ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from("accessToken", token)
                .httpOnly(false)               // TODO: 배포 시 true
                .secure(false)                 // TODO: 배포 시 true
                .sameSite("Lax")               // TODO: 배포 시 None
                .path("/")
                .maxAge(Duration.ofDays(1))    // 쿠키 유효시간
                .build();
    }
}
