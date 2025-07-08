package com.mobble.mobbleserver.global.exception.errorCode.member;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    JOINED_EMAIL("이미 가입된 email 입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_MEMBER("멤버를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);


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
