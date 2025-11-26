package com.mobble.mobbleserver.application.comment.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommentBusinessError implements CommonError {
    NOT_FOUND("comment not found", ErrorCategory.NOT_FOUND),
    NO_PERMISSION("do not have permission to access this comment", ErrorCategory.PERMISSION_DENIED),
    ARTICLE_MISMATCH("comment does not belong to the requested article", ErrorCategory.VALIDATION);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "COMMENT_BUSINESS." + name();
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
