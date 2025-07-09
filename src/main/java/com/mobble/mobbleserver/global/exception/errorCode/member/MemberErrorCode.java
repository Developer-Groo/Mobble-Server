package com.mobble.mobbleserver.global.exception.errorCode.member;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_ALREADY_EXISTS("이미 가입된 email 입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_MEMBER("멤버를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAILED_JOIN("탈퇴한 회원은 7일 후 가입할 수 있습니다.", HttpStatus.FORBIDDEN);


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
