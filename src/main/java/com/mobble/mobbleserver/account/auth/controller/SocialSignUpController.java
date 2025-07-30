package com.mobble.mobbleserver.account.auth.controller;

import com.mobble.mobbleserver.account.auth.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.account.auth.service.SocialSignUpService;
import com.mobble.mobbleserver.account.auth.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SocialSignUpController {

    private final SocialSignUpService socialSignUpService;

    @PostMapping("/social-signup")
    public ResponseEntity<Void> signUp(
            @RequestBody @Valid SignUpRequestDto dto
    ) {
        String accessToken = socialSignUpService.signUp(dto);
        ResponseCookie cookie = CookieUtil.createAccessTokenCookie(accessToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", cookie.toString())
                .body(null);
    }
}
