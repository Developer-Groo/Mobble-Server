package com.mobble.mobbleserver.account.auth.controller;

import com.mobble.mobbleserver.account.auth.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.account.auth.dto.response.SignUpDetailsInfoResponseDto;
import com.mobble.mobbleserver.account.auth.service.SignUpDetailsInfoService;
import com.mobble.mobbleserver.account.auth.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SignUpDetailsInfoController {

    private final SignUpDetailsInfoService signUpDetailsInfoService;

    @GetMapping("/details-info")
    public ResponseEntity<SignUpDetailsInfoResponseDto> getSocialUserInfo(
            @CookieValue("accessToken") String signupToken
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(signUpDetailsInfoService.getSocialUserInfo(signupToken));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signup(
            @CookieValue("accessToken") String signupToken,
            @RequestBody SignUpRequestDto dto
    ) {
        ResponseCookie cookie = CookieUtil.createAccessTokenCookie(signUpDetailsInfoService.signup(signupToken, dto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", cookie.toString())
                .body(null);
    }
}
