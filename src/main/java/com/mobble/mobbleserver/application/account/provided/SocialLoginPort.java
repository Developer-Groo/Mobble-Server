package com.mobble.mobbleserver.application.account.provided;

import com.mobble.mobbleserver.application.account.command.SocialLoginCommand;
import com.mobble.mobbleserver.application.account.result.SocialLoginResult;

public interface SocialLoginPort {

    SocialLoginResult socialLogin(SocialLoginCommand command);
}
