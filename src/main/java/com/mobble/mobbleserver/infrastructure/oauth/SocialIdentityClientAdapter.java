package com.mobble.mobbleserver.infrastructure.oauth;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.application.account.required.SocialIdentityClientPort;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;
import com.mobble.mobbleserver.infrastructure.oauth.provider.SocialIdentityClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SocialIdentityClientAdapter implements SocialIdentityClientPort {

    private final Map<SocialProvider, SocialIdentityClient> clientMap;

    public SocialIdentityClientAdapter(List<SocialIdentityClient> clients) {
        this.clientMap = clients.stream()
                .collect(Collectors.toMap(
                        SocialIdentityClient::getProvider,
                        client -> client
                ));
    }

    @Override
    public SocialUserInfo fetchUserInfo(SocialProvider socialProvider, String token) {
        SocialIdentityClient client = clientMap.get(socialProvider);
        if (client == null) throw new DomainException(OAuthErrorCode.UNSUPPORTED_SOCIAL_PROVIDER);

        return client.fetchUserInfo(token);
    }
}
