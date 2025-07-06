package com.mobble.mobbleserver.global.exception.common;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String message();

    HttpStatus httpStatus();
}
