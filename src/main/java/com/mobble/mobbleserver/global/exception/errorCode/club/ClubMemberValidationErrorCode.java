package com.mobble.mobbleserver.global.exception.errorCode.club;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ClubMemberValidationErrorCode implements ErrorCode {
    MEMBER_ID_NOT_NULL("변경할 멤버의 ID를 입력해주세요.", HttpStatus.BAD_REQUEST),
    STATUS_NOT_NULL("변경 상태를 선택해주세요.", HttpStatus.BAD_REQUEST),
    ROLE_NOT_NULL("변경 권한을 선택해주세요.", HttpStatus.BAD_REQUEST);

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
