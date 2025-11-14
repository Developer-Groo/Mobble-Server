package com.mobble.mobbleserver.application.account.required;

import com.mobble.mobbleserver.application.account.command.SocialUserInfo;

public interface SignUpTokenParsePort {

    SocialUserInfo getSignUpTokenInfo(String signUpToken);
}
