package com.mobble.mobbleserver.global.exception.errorCode.club;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor

public enum ClubErrorCode implements ErrorCode {
    NOT_FOUND("클럽을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CATEGORY_REQUIRED("클럽 카테고리는 필수 입니다.", HttpStatus.BAD_REQUEST),
    NAME_REQUIRED("클럽 이름은 필수 입니다.", HttpStatus.BAD_REQUEST),
    GROUND_REQUIRED("클럽 활동지역은 필수 입니다.", HttpStatus.BAD_REQUEST),
    ADDRESS_REQUIRED("클럽 주소는 필수 입니다.", HttpStatus.BAD_REQUEST),
    HEADCOUNT_REQUIRED("클럽 정원 수는 2명 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND("알수 없는 클럽 카테고리입니다.", HttpStatus.BAD_REQUEST),
    NO_PERMISSION("해당 클럽을 수정 또는 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);

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
