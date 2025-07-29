package com.mobble.mobbleserver.account.oauth2.verifier;

import com.mobble.mobbleserver.account.oauth2.service.SocialProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialVerifierFactory {

    private final KakaoTokenVerifier kakaoTokenVerifier;
    private final NaverTokenVerifier naverTokenVerifier;
    private final GoogleTokenVerifier googleTokenVerifier;

    public SocialVerifier getVerifier(SocialProvider socialProvider) {
        return switch (socialProvider) {
            case KAKAO -> kakaoTokenVerifier;
            case NAVER -> naverTokenVerifier;
            case GOOGLE -> googleTokenVerifier;
        };
    }
}
