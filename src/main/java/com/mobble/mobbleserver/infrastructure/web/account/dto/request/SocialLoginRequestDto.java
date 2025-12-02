package com.mobble.mobbleserver.infrastructure.web.account.dto.request;

import com.mobble.mobbleserver.application.account.command.SocialLoginCommand;
import com.mobble.mobbleserver.application.account.command.SocialProvider;

public record SocialLoginRequestDto(String accessToken, SocialProvider socialProvider) {

    public SocialLoginCommand toCommand() {
        return SocialLoginCommand.create(accessToken, socialProvider);
    }
}
