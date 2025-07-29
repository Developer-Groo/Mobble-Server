package com.mobble.mobbleserver.account.oauth2.verifier;

public interface SocialVerifier {
    SocialUserInfo verify(String accessToken);
}
