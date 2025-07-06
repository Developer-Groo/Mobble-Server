package com.mobble.mobbleserver.global.exception.errorCode.validation;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ValidationErrorCode implements ErrorCode {
    FIELD_VALIDATION_ERROR("필드 검증에 실패했습니다.", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER("필수 요청값이 누락되었습니다.", HttpStatus.BAD_REQUEST),
    TYPE_MISMATCH("요청 파라미터 타입이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
