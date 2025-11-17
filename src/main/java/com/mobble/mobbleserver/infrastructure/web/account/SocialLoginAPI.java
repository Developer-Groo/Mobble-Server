package com.mobble.mobbleserver.infrastructure.web.account;

import com.mobble.mobbleserver.application.account.command.SocialLoginResult;
import com.mobble.mobbleserver.application.account.provided.SocialLoginPort;
import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SocialLoginRequestDto;
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
public class SocialLoginAPI {

    private final SocialLoginPort socialLoginPort;

    @PostMapping("/login")
    public ResponseEntity<Void> socialLogin(
            @RequestBody @Valid SocialLoginRequestDto dto
    ) {
        SocialLoginResult result = socialLoginPort.socialLogin(dto);

        return ResponseEntity.status(result.isNewMember() ? HttpStatus.FORBIDDEN : HttpStatus.OK)
                .header("Authorization", "Bearer " + result.jwtToken())
                .build();
    }
}
