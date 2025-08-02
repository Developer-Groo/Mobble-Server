package com.mobble.mobbleserver.account.auth.oauth.verifier;

public interface SocialVerifier {
    SocialUserInfo verify(String accessToken);
}
