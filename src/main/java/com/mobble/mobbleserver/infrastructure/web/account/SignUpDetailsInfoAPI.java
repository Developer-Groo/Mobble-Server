package com.mobble.mobbleserver.infrastructure.web.account;

import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.infrastructure.web.account.dto.response.SignUpDetailsInfoResponseDto;
import com.mobble.mobbleserver.application.account.service.SignUpDetailsInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SignUpDetailsInfoAPI {

    private final SignUpDetailsInfoService signUpDetailsInfoService;

    @GetMapping("/details-info")
    public ResponseEntity<SignUpDetailsInfoResponseDto> getSocialUserInfo(
            @RequestHeader("Authorization") String authHeader
    ) {
        String signupToken = authHeader.replace("Bearer ", "");

        return ResponseEntity.status(HttpStatus.OK)
                .body(signUpDetailsInfoService.getSocialUserInfo(signupToken));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signup(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SignUpRequestDto dto
    ) {
        String signupToken = authHeader.replace("Bearer ", "");
        String jwtToken = signUpDetailsInfoService.signup(signupToken, dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", "Bearer " + jwtToken)
                .build();
    }
}
