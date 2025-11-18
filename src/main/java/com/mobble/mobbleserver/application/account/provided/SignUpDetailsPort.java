package com.mobble.mobbleserver.application.account.provided;

import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SignUpRequestDto;

public interface SignUpDetailsPort {

    SocialUserInfo getSocialUserInfo(String signupToken);

    String signUp(String signUpToken, SignUpRequestDto dto);
}
