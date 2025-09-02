package com.mobble.mobbleserver.global.exception.errorCode.meeting;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MeetingErrorCode implements ErrorCode {
    NOT_FOUND_MEETING("모임을 찾을 수 없습니다.", HttpStatus.NOT_FOUND)

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
