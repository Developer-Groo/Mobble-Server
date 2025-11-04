package com.mobble.mobbleserver.shared.error;

public interface CommonError {

    String code();

    String message();

    ErrorCategory category();
}
