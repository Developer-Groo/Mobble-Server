package com.mobble.mobbleserver.infrastructure.oauth.provider.userInfoResult;

import com.mobble.mobbleserver.application.account.command.SocialProvider;

public interface OAuth2UserInfo {

    SocialProvider getProvider();

    String getProviderId();

    String getEmail();
}
