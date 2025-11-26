package com.mobble.mobbleserver.domain.like.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LikeError implements CommonError {
    REQUIRED_TARGET("targetId must not be null", ErrorCategory.VALIDATION),
    REQUIRED_LIKE_TYPE("likeType must not be null", ErrorCategory.VALIDATION);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "Like." + name();
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
