package com.mobble.mobbleserver.application.exception;

import com.mobble.mobbleserver.shared.error.CommonError;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final CommonError error;

    public BusinessException(CommonError error) {
        super(error.message());
        this.error = error;
    }
}
