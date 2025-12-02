package com.mobble.mobbleserver.application.account.provided;

import com.mobble.mobbleserver.application.account.command.SignUpCommand;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;

public interface SignUpDetailsPort {

    SocialUserInfo getSocialUserInfo(String signupToken);

    String signUp(SignUpCommand command);
}
