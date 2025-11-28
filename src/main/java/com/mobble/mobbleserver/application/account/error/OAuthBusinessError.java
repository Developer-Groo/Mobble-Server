package com.mobble.mobbleserver.application.account.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OAuthBusinessError implements CommonError {
    INVALID_ACCESS_TOKEN("invalid access token", ErrorCategory.PERMISSION_DENIED),
    SOCIAL_PROVIDER_UNAVAILABLE("unavailable social provider", ErrorCategory.UNAVAILABLE),
    INVALID_USER_INFO("user info is invalid", ErrorCategory.VALIDATION),
    INVALID_SOCIAL_PROVIDER("invalid social provider", ErrorCategory.VALIDATION);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "ACCOUNT_BUSINESS" + name();
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public ErrorCategory category() {
        return category;
    }
}
