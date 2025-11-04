package com.mobble.mobbleserver.domain.common.exception;

import com.mobble.mobbleserver.shared.error.CommonError;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private final CommonError error;

    public DomainException(CommonError error) {
        super(error.message());
        this.error = error;
    }
}
