package com.mobble.mobbleserver.account.auth.oauth.verifier;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.account.auth.oauth.verifier.provider.GoogleTokenVerifier;
import com.mobble.mobbleserver.account.auth.oauth.verifier.provider.KakaoTokenVerifier;
import com.mobble.mobbleserver.account.auth.oauth.verifier.provider.NaverTokenVerifier;
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
