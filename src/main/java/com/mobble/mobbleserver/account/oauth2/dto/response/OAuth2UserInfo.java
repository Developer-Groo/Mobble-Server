package com.mobble.mobbleserver.account.oauth2.dto.response;

import com.mobble.mobbleserver.account.oauth2.provider.SocialProvider;

public interface OAuth2UserInfo {

    SocialProvider getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
