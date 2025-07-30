package com.mobble.mobbleserver.account.auth.controller;

import com.mobble.mobbleserver.account.auth.dto.request.SocialLoginRequestDto;
import com.mobble.mobbleserver.account.auth.service.SocialLoginService;
import com.mobble.mobbleserver.account.auth.util.CookieUtil;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
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
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @PostMapping("/social-login")
    public ResponseEntity<Void> socialLogin(
            @RequestBody @Valid SocialLoginRequestDto dto
    ) {
        try {
            String accessToken = socialLoginService.socialLogin(dto);
            ResponseCookie cookie = CookieUtil.createAccessTokenCookie(accessToken);

            return ResponseEntity.status(HttpStatus.OK)
                    .header("Set-Cookie", cookie.toString())
                    .body(null);
        } catch (DomainException e) {
            if (e.getErrorCode() == MemberErrorCode.NOT_FOUND_MEMBER) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            throw e;
        }
    }
}
