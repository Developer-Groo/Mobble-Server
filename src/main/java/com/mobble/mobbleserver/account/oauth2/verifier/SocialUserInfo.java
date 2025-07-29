package com.mobble.mobbleserver.account.oauth2.verifier;

import com.mobble.mobbleserver.account.oauth2.service.SocialProvider;

public record SocialUserInfo(
        String name,
        String email,
        SocialProvider socialProvider,
        String socialId
) {
}
