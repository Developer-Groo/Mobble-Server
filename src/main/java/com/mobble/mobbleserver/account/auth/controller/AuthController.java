package com.mobble.mobbleserver.account.auth.controller;

import com.mobble.mobbleserver.account.auth.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.account.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<String> signUp(
            @ModelAttribute @Valid SignUpRequestDto dto
    ) {
        authService.registerNewUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("회원가입이 완료되었습니다.");
    }
}
