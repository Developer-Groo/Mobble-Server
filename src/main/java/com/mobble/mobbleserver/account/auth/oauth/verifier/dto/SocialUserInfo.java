package com.mobble.mobbleserver.account.auth.oauth.verifier.dto;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;

public record SocialUserInfo(
        String email,
        SocialProvider socialProvider,
        String socialId
) {
}
