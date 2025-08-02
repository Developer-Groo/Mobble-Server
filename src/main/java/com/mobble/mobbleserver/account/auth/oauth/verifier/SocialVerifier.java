package com.mobble.mobbleserver.account.auth.oauth.verifier;

import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;

public interface SocialVerifier {
    SocialUserInfo verify(String accessToken);
}
