package com.mobble.mobbleserver.global.exception.handler;

import com.mobble.mobbleserver.global.exception.errorCode.global.GlobalErrorCode;
import com.mobble.mobbleserver.global.exception.handler.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(3)
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 클라이언트의 요청 본문이 JSON 형식이 아닐 경우 처리
     * 예: JSON 문법 오류, 잘못된 필드 타입 등
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidJson() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.toDto(GlobalErrorCode.INVALID_JSON));
    }

    /**
     * 클라이언트가 지원하지 않는 Content-Type 으로 요청을 보낸 경우 처리
     * 예: Content-Type 이 application/json 이 아닌 경우
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleMediaTypeNotSupported() {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(ErrorResponseDto.toDto(GlobalErrorCode.UNSUPPORTED_MEDIA_TYPE));
    }

    /**
     * 예상하지 못한 모든 예외(Exception)를 처리
     * 예: 위에서 처리되지 않은 런타임 예외 or 시스템 오류 등이 발생한 경우
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleInternalServerError(Exception ex, HttpServletRequest request) {
        log.error("""
                        🚨 Unexpected Exception occurred 🚨
                        [Type]   : {}
                        [Message]: {}
                        [URI]    : {} {}
                        [IP]     : {}
                        """,
                ex.getClass().getName(),
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                ex
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.toDto(GlobalErrorCode.INTERNAL_SERVER_ERROR));
    }
}
