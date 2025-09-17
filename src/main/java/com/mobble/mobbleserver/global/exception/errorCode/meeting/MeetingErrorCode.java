package com.mobble.mobbleserver.global.exception.errorCode.meeting;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MeetingErrorCode implements ErrorCode {
    NOT_FOUND_MEETING("모임을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TITLE_REQUIRED("모임 제목은 필수입니다.", HttpStatus.BAD_REQUEST),
    DATETIME_REQUIRED("모임 일시는 필수입니다.", HttpStatus.BAD_REQUEST),
    LOCATION_REQUIRED("모임 장소는 필수입니다.", HttpStatus.BAD_REQUEST),
    COST_REQUIRED("비용 정보는 필수입니다.", HttpStatus.BAD_REQUEST),
    INVALID_MEMBER_LIMIT("참여 인원은 1명 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    TYPE_REQUIRED("모임 유형은 필수입니다.", HttpStatus.BAD_REQUEST),
    CLUB_MEMBER_REQUIRED("작성자는 필수입니다.", HttpStatus.BAD_REQUEST);

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
