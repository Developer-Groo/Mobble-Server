package com.mobble.mobbleserver.global.exception.errorCode.article;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ArticleValidationErrorCode implements ErrorCode {
    TITLE_NOT_BLANK("게시글 제목을 입력해주세요.", HttpStatus.BAD_REQUEST),
    TITLE_TOO_LONG("게시글 제목은 최대 30자까지 입력 가능합니다.", HttpStatus.BAD_REQUEST),
    CONTENT_NOT_BLANK("게시글 내용을 입력해주세요.", HttpStatus.BAD_REQUEST),
    CONTENT_TOO_LONG("게시글 내용은 최대 800자까지 입력 가능합니다.", HttpStatus.BAD_REQUEST);

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
