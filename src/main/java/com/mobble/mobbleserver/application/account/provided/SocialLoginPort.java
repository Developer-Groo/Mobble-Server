package com.mobble.mobbleserver.application.account.provided;

import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SocialLoginRequestDto;
import com.mobble.mobbleserver.application.account.command.SocialLoginResult;

public interface SocialLoginPort {

    SocialLoginResult socialLogin(SocialLoginRequestDto dto);
}
