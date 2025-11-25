package com.mobble.mobbleserver.infrastructure.oauth;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.application.account.required.SocialIdentityClientPort;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;
import com.mobble.mobbleserver.infrastructure.oauth.common.AbstractSocialClient;
import com.mobble.mobbleserver.infrastructure.oauth.common.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SocialIdentityClientAdapter implements SocialIdentityClientPort {

    private final List<AbstractSocialClient> clientList;

    @Override
    public SocialUserInfo verify(SocialProvider socialProvider, String token) {
        AbstractSocialClient client = findClient(socialProvider);

        OAuth2UserInfo userInfo = client.fetchUserInfo(token);

        return new SocialUserInfo(
                userInfo.getEmail(),
                userInfo.getProvider(),
                userInfo.getProviderId()
        );
    }

    private AbstractSocialClient findClient(SocialProvider provider) {
        return clientList.stream()
                .filter(client -> provider.equals(client.getProvider()))
                .findFirst()
                .orElseThrow(() -> new DomainException(OAuthErrorCode.UNSUPPORTED_SOCIAL_PROVIDER));
    }
}
