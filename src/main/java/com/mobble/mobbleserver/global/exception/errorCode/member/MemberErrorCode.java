package com.mobble.mobbleserver.global.exception.errorCode.member;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    NOT_FOUND_MEMBER("멤버를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_ALREADY_EXISTS("이미 가입된 회원입니다.", HttpStatus.BAD_REQUEST),
    FAILED_JOIN("탈퇴한 회원은 7일 후 가입할 수 있습니다.", HttpStatus.FORBIDDEN),
    NAME_REQUIRED("이름은 필수입니다.", HttpStatus.BAD_REQUEST),
    AGE_REQUIRED("나이는 필수입니다.", HttpStatus.BAD_REQUEST),
    GENDER_REQUIRED("성별은 필수입니다.", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED("이메일은 필수입니다.", HttpStatus.BAD_REQUEST),
    PHONE_REQUIRED("전화번호는 필수입니다.", HttpStatus.BAD_REQUEST),
    GROUND_REQUIRED("활동지역 선택은 필수입니다.", HttpStatus.BAD_REQUEST),
    TERMS_AGREED_REQUIRED("서비스 이용 약관 동의는 필수입니다.", HttpStatus.BAD_REQUEST),
    PRIVACY_AGREED_REQUIRED("개인정보 수집 및 이용동의는 필수입니다.", HttpStatus.BAD_REQUEST);

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
