package com.mobble.mobbleserver.account.auth.oauth.dto.response;

import com.mobble.mobbleserver.application.account.command.SocialProvider;

public interface OAuth2UserInfo {

    SocialProvider getProvider();

    String getProviderId();

    String getName();

    String getEmail();
}
