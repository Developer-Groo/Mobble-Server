package com.mobble.mobbleserver.domain.comment.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommentError implements CommonError {
    INVALID_PARENT("parent must be a root comment", ErrorCategory.VALIDATION),
    EMPTY_CONTENT("content must not be empty", ErrorCategory.VALIDATION),
    CONTENT_TOO_LONG("content length must be less than or equal to 150", ErrorCategory.VALIDATION);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "COMMENT." + name();
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
