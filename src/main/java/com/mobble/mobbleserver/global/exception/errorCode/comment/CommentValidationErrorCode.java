package com.mobble.mobbleserver.global.exception.errorCode.comment;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommentValidationErrorCode implements ErrorCode {
    CONTENT_NOT_BLANK("댓글 내용을 입력해주세요.", HttpStatus.BAD_REQUEST),
    CONTENT_TOO_LONG("댓글은 최대 100자까지 입력 가능합니다.", HttpStatus.BAD_REQUEST);

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
