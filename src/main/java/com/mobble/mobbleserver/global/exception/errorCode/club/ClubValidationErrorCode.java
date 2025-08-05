package com.mobble.mobbleserver.global.exception.errorCode.club;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ClubValidationErrorCode implements ErrorCode {

    NAME_NOT_BLANK("클럽 이름을 입력해주세요.", HttpStatus.BAD_REQUEST),
    NAME_TOO_LONG("클럽 이름은 최대 20자 까지 입력 가능합니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_BLANK("클럽 카테고리를 선택해주세요.", HttpStatus.BAD_REQUEST),
    GROUND_NOT_BLANK("클럽 활동 지역을 선택해주세요.", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_BLANK("클럽 주소를 입력해주세요.", HttpStatus.BAD_REQUEST),
    HEADCOUNT_MIN("클럽 정원수는 2명 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    HEADCOUNT_MAX("클럽 정원수는 1000명을 넘길 수 없습니다.", HttpStatus.BAD_REQUEST),
    AGE_GROUP_NOT_EMPTY("클럽 연령층을 선택해주세요.", HttpStatus.BAD_REQUEST),
    JOIN_TYPE_REQUIRED("클럽 가입유형을 선택해주세요.", HttpStatus.BAD_REQUEST);


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
