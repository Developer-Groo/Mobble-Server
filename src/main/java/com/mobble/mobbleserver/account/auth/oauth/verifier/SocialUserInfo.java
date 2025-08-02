package com.mobble.mobbleserver.account.auth.oauth.verifier;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;

public record SocialUserInfo(
        String name,
        String email,
        SocialProvider socialProvider,
        String socialId
) {
}
