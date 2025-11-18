package com.mobble.mobbleserver.infrastructure.web.account;

import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.application.account.provided.SignUpDetailsPort;
import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.infrastructure.web.account.dto.response.SignUpDetailsInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SignUpDetailsInfoAPI {

    private final SignUpDetailsPort signUpDetailsPort;

    @GetMapping("/details-info")
    public ResponseEntity<SignUpDetailsInfoResponseDto> getSocialUserInfo(
            @RequestHeader("Authorization") String authHeader
    ) {
        String signUpToken = authHeader.replace("Bearer ", "");

        SocialUserInfo userInfo = signUpDetailsPort.getSocialUserInfo(signUpToken);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SignUpDetailsInfoResponseDto.toDto(userInfo));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signup(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SignUpRequestDto dto
    ) {
        String signupToken = authHeader.replace("Bearer ", "");
        String jwtToken = signUpDetailsPort.signUp(signupToken, dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", "Bearer " + jwtToken)
                .build();
    }
}
