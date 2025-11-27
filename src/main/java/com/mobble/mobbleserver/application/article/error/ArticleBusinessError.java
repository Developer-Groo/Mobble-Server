package com.mobble.mobbleserver.application.article.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ArticleBusinessError implements CommonError {
    NOT_FOUND("article not found", ErrorCategory.NOT_FOUND),
    NO_PERMISSION("do not have permission to access this article", ErrorCategory.PERMISSION_DENIED),
    CLUB_MISMATCH("article does not belong to this club", ErrorCategory.VALIDATION);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "ARTICLE_BUSINESS." + name();
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
