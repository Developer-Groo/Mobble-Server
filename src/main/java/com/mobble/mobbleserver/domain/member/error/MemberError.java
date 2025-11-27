package com.mobble.mobbleserver.domain.member.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberError implements CommonError {
    REQUIRED_TERMS_AGREED("agreement to the terms of service is required", ErrorCategory.VALIDATION),
    REQUIRED_PRIVACY_AGREED("agreement to the privacy policy is required", ErrorCategory.VALIDATION),
    INVALID_GENDER("invalid gender value", ErrorCategory.VALIDATION);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "MEMBER." + name();
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
