package com.mobble.mobbleserver.global.exception.handler;

import com.mobble.mobbleserver.global.exception.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * 클라이언트의 요청 본문이 JSON 형식이 아닐 경우 처리
     * 예: JSON 문법 오류, 잘못된 필드 타입 등
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidJson(Locale locale) {
        String message = messageSource.getMessage("error.invalid_json", null, locale);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.toDto(message, HttpStatus.BAD_REQUEST));
    }

    /**
     * 클라이언트가 지원하지 않는 Content-Type 으로 요청을 보낸 경우 처리
     * 예: Content-Type 이 application/json 이 아닌 경우
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleMediaTypeNotSupported(Locale locale) {
        String message = messageSource.getMessage("error.unsupported_media_type", null, locale);

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(ErrorResponseDto.toDto(message, HttpStatus.UNSUPPORTED_MEDIA_TYPE));
    }
}
