package com.mobble.mobbleserver.global.exception.dto;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ErrorResponseDto(String message, HttpStatus code, Map<String, String> errors) {

    // Validation Error
    public static ErrorResponseDto toDto(String message, Map<String, String> errors) {
        return new ErrorResponseDto(message, HttpStatus.BAD_REQUEST, errors);
    }

    // Global & Domain Error
    public static ErrorResponseDto toDto(String message, HttpStatus code) {
        return new ErrorResponseDto(message, code, null);
    }
}
