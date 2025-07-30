package com.mobble.mobbleserver.account.auth.controller;

import com.mobble.mobbleserver.account.auth.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.account.auth.service.SocialSignUpService;
import com.mobble.mobbleserver.account.auth.util.CookieUtil;
import com.mobble.mobbleserver.account.oauth2.service.SocialProvider;
import com.mobble.mobbleserver.account.oauth2.verifier.SocialUserInfo;
import com.mobble.mobbleserver.account.oauth2.verifier.SocialVerifierFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SocialSignUpController {

    private final SocialSignUpService socialSignUpService;
    private final SocialVerifierFactory verifierFactory;

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

    // 서버에서 accessToken으로 사용자 정보 요청 API
    @GetMapping("/details-info")
    public ResponseEntity<SocialUserInfo> getSocialUserInfo(
            @CookieValue("accessToken") String accessToken,
            @RequestParam("socialProvider") String provider
    ) {
        SocialProvider socialProvider = SocialProvider.fromString(provider);

        return ResponseEntity.status(HttpStatus.OK)
                .body(verifierFactory.getVerifier(socialProvider).verify(accessToken));
    }
}
