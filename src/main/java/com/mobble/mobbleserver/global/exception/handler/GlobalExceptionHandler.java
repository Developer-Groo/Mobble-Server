package com.mobble.mobbleserver.global.exception.handler;

import com.mobble.mobbleserver.global.exception.errorCode.global.GlobalErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.security.SecurityErrorCode;
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
import org.springframework.web.servlet.resource.NoResourceFoundException;

import org.springframework.security.access.AccessDeniedException;

@Slf4j
@Order(3)
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 존재하지 않는 정적 리소스 요청이 들어온 경우 처리
     * 예: URL 오타 등으로 잘못된 요청 경로
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoResourceFoundException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.toDto(GlobalErrorCode.NOT_FOUND));
    }

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
     * 인가되지 않은 사용자가 권한 검증이 필요한 리소스에 접근하려고 할 때 처리
     * 예: 일반 회원(MEMBER)이 관리자 전용(LEADER, MANAGER) 권한 API에 접근할 경우
     * #   @PreAuthorize 조건이 false를 반환한 경우
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("❌ Access Denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponseDto.toDto(SecurityErrorCode.ACCESS_DENIED));
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
