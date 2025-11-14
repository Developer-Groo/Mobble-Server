package com.mobble.mobbleserver.application.account.provided;

import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SignUpRequestDto;

public interface SignUpDetailsPort {

    void getSocialUserInfo(String signupToken);

    void signUp(String signUpToken, SignUpRequestDto dto);
}
