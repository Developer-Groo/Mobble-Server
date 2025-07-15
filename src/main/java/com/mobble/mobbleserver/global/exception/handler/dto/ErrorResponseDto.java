package com.mobble.mobbleserver.global.exception.handler.dto;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public record ErrorResponseDto(
        String timestamp,
        String message,
        int status,
        String code,
        Map<String, String> errors
) {

    // Validation or Binding Error
    public static ErrorResponseDto toDto(ErrorCode errorCode, Map<String, String> errors) {
        return new ErrorResponseDto(
                createDateTime(),
                errorCode.message(),
                errorCode.httpStatus().value(),
                errorCode.httpStatus().name(),
                errors
        );
    }

    // Global or Domain Error
    public static ErrorResponseDto toDto(ErrorCode errorCode) {
        return new ErrorResponseDto(
                createDateTime(),
                errorCode.message(),
                errorCode.httpStatus().value(),
                errorCode.httpStatus().name(),
                null
        );
    }

    private static String createDateTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
