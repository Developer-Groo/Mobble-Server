package com.mobble.mobbleserver.infrastructure.oauth.provider;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;

public interface SocialIdentityClient {

    SocialProvider getProvider();

    SocialUserInfo fetchUserInfo(String token);
}
