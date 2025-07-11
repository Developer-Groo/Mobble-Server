package com.mobble.mobbleserver.global.exception.errorCode.member;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemberValidationErrorCode implements ErrorCode {
    NAME_NOT_BLANK("이름을 입력해주세요.", HttpStatus.BAD_REQUEST),
    NAME_TOO_LONG("이름은 최대 10자까지 입력 가능합니다..", HttpStatus.BAD_REQUEST),
    AGE_TOO_LOW("나이는 1세 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    AGE_TOO_HIGH("나이는 100세 이하로 입력해주세요.", HttpStatus.BAD_REQUEST),
    REQUIRED_GENDER("성별을 입력해주세요.", HttpStatus.BAD_REQUEST),
    INVALID_GENDER("성별을 올바르게 입력해주세요.", HttpStatus.BAD_REQUEST),
    REQUIRED_EMAIL("이메일을 입력해주세요.", HttpStatus.BAD_REQUEST),
    WRONG_EMAIL_PATTERN("이메일 형식이 올바르지 않습니다. (ex.example@email.com)", HttpStatus.BAD_REQUEST),
    REQUIRED_PHONE("전화번호를 입력해주세요.", HttpStatus.BAD_REQUEST),
    WRONG_PHONE_PATTERN("전화번호 형식이 올바르지 않습니다. (ex. 010-1234-5678)", HttpStatus.BAD_REQUEST),
    REQUIRED_GROUND("활동지역을 입력해주세요.", HttpStatus.BAD_REQUEST),
    REQUIRED_TERMS_AGREE("서비스 이용 약관 동의는 필수입니다.", HttpStatus.BAD_REQUEST),
    REQUIRED_PRIVACY_AGREE("개인정보 수집 및 이용 동의는 필수입니다.", HttpStatus.BAD_REQUEST);

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
