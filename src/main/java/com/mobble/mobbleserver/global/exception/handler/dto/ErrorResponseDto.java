package com.mobble.mobbleserver.global.exception.handler.dto;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Map;

public record ErrorResponseDto(String message, HttpStatus code, Map<String, String> errors) {

    // Validation or Binding Error
    public static ErrorResponseDto toDto(ErrorCode errorCode, Map<String, String> errors) {
        return new ErrorResponseDto(errorCode.message(), errorCode.httpStatus(), errors);
    }

    // Global or Domain Error
    public static ErrorResponseDto toDto(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode.message(), errorCode.httpStatus(), null);
    }
}
