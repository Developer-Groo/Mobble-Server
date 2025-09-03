package com.mobble.mobbleserver.global.exception.errorCode.meeting;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MeetingMemberErrorCode implements ErrorCode {
    EXCEED_MEETING_MEMBER("더 이상 참석할 수 없습니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public String message() {
        return message();
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus();
    }
}
