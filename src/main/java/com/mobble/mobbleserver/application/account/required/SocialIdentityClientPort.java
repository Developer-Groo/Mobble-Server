package com.mobble.mobbleserver.application.account.required;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;

public interface SocialIdentityClientPort {

    SocialUserInfo verify(SocialProvider socialProvider, String accessToken);
}
