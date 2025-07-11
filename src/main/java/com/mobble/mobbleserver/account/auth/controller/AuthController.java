package com.mobble.mobbleserver.account.auth.controller;

import com.mobble.mobbleserver.account.auth.dto.request.LoginRequestDto;
import com.mobble.mobbleserver.account.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Long> login(
            @RequestBody LoginRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.login(dto));
    }
}
