package com.mobble.mobbleserver.account.auth.oauth.verifier;

import com.mobble.mobbleserver.application.account.command.SocialUserInfo;

public interface SocialVerifier {

    SocialUserInfo verify(String accessToken);
}
