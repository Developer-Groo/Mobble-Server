package com.mobble.mobbleserver.account.auth.controller;

import com.mobble.mobbleserver.account.auth.dto.request.SocialLoginRequestDto;
import com.mobble.mobbleserver.account.auth.dto.response.SocialLoginResponseDto;
import com.mobble.mobbleserver.account.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthLoginController {

    private final AuthService authService;

    @PostMapping("/social-login")
    public ResponseEntity<SocialLoginResponseDto> socialLogin(
            @RequestBody @Valid SocialLoginRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.socialLoginAndSignUp(dto));
    }
}
