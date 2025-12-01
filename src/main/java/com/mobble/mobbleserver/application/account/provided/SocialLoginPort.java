package com.mobble.mobbleserver.application.account.provided;

import com.mobble.mobbleserver.application.account.result.SocialLoginResult;
import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SocialLoginRequestDto;

public interface SocialLoginPort {

    SocialLoginResult socialLogin(SocialLoginRequestDto dto);
}
