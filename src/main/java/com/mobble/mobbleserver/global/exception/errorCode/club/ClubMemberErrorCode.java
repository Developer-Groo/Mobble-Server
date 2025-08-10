package com.mobble.mobbleserver.global.exception.errorCode.club;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor

public enum ClubMemberErrorCode implements ErrorCode {
    ALREADY_JOINED("이미 해당 클럽에 가입 또는 신청 된 상태입니다.", HttpStatus.CONFLICT),
    NOT_JOINED_CLUB("해당 클럽에 가입된 회원이 아닙니다.", HttpStatus.FORBIDDEN),
    CLUB_IS_FULL("클럽 정원이 초과되어 가입 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_CHANGE_OWN_ROLE("본인의 권한은 변경 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NO_PERMISSION("수정 또는 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);

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
