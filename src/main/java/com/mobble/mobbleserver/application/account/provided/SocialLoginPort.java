package com.mobble.mobbleserver.application.account.provided;

import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SocialLoginRequestDto;
import com.mobble.mobbleserver.infrastructure.web.account.dto.response.SocialLoginResponseDto;

public interface SocialLoginPort {

    SocialLoginResponseDto socialLogin(SocialLoginRequestDto dto);
}
