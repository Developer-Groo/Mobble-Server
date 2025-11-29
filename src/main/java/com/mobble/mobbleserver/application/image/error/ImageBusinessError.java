package com.mobble.mobbleserver.application.image.error;

import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.shared.error.ErrorCategory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageBusinessError implements CommonError {
    NOT_FOUND("image not found", ErrorCategory.NOT_FOUND),
    INVALID_IMAGE_SIZE("invalid image size", ErrorCategory.CONFLICT),
    INVALID_IMAGE_TYPE("invalid image type", ErrorCategory.CONFLICT);

    private final String message;
    private final ErrorCategory category;

    @Override
    public String code() {
        return "IMAGE_BUSINESS." + name();
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
