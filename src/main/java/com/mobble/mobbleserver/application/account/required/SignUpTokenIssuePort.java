package com.mobble.mobbleserver.application.account.required;

import com.mobble.mobbleserver.application.account.command.SocialProvider;

public interface SignUpTokenIssuePort {

    String createSignUpToken(String email, SocialProvider socialProvider, String socialId);
}
