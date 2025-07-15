package com.mobble.mobbleserver.global.exception.errorCode.global;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    INVALID_JSON("잘못된 JSON 요청 형식입니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_MEDIA_TYPE("지원하지 않는 Content-Type 입니다.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    INTERNAL_SERVER_ERROR("알 수 없는 서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

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
