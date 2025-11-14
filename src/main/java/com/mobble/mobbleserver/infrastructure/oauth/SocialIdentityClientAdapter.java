package com.mobble.mobbleserver.infrastructure.oauth;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.application.account.required.SocialIdentityClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialIdentityClientAdapter implements SocialIdentityClientPort {

    private final AppleClient appleClient;
    private final GoogleClient googleClient;
    private final NaverClient naverClient;
    private final KakaoClient kakaoClient;

    @Override
    public SocialUserInfo fetchUserInfo(SocialProvider socialProvider, String accessToken) {
        return switch (socialProvider) {
            case APPLE -> appleClient.fetchUserInfo(accessToken);
            case GOOGLE -> googleClient.fetchUserInfo(accessToken);
            case NAVER -> naverClient.fetchUserInfo(accessToken);
            case KAKAO -> kakaoClient.fetchUserInfo(accessToken);
        };
    }
}
