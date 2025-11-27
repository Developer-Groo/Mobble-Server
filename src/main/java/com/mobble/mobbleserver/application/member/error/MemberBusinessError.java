package com.mobble.mobbleserver.application.member.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberBusinessError implements CommonError {
    NOT_FOUND("member not found", ErrorCategory.NOT_FOUND),
    FAILED_JOIN("deleted member can rejoin after 7 days", ErrorCategory.PERMISSION_DENIED);


    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "MEMBER_BUSINESS" + name();
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
