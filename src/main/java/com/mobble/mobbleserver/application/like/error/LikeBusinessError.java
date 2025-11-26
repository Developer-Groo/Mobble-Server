package com.mobble.mobbleserver.application.like.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LikeBusinessError implements CommonError {
    INVALID_LIKE_TYPE("unsupported like type", ErrorCategory.VALIDATION),
    TARGET_NOT_FOUND("target not found", ErrorCategory.NOT_FOUND);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "MEETING_BUSINESS" + name();
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
