package com.mobble.mobbleserver.domain.article.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ArticleError implements CommonError {
    EMPTY_CONTENT("content must not be empty", ErrorCategory.VALIDATION),
    CONTENT_TOO_LONG("content length must be less than or equal to 200", ErrorCategory.VALIDATION);;

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "ARTICLE." + name();
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
