package com.mobble.mobbleserver.application.account.required;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;

public interface SignUpTokenPort {

    String issueSignUpToken(String email, SocialProvider socialProvider, String socialId);

    SocialUserInfo extractSignUpInfo(String signUpToken);
}
