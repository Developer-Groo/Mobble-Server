package com.mobble.mobbleserver.account.auth.controller;

import com.mobble.mobbleserver.account.auth.dto.request.SocialLoginRequestDto;
import com.mobble.mobbleserver.account.auth.dto.response.SocialLoginResponseDto;
import com.mobble.mobbleserver.account.auth.service.SocialLoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        SocialLoginResponseDto result = socialLoginService.socialLogin(dto);

        return ResponseEntity.status(result.isNewMember() ? HttpStatus.UNAUTHORIZED : HttpStatus.OK)
                .header("Authorization", "Bearer " + result.accessJwtToken())
                .build();
    }
}
