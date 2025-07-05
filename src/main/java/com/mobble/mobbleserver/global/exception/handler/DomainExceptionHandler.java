package com.mobble.mobbleserver.global.exception.handler;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import com.mobble.mobbleserver.global.exception.handler.dto.ErrorResponseDto;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(2)
@RestControllerAdvice
public class DomainExceptionHandler {

    /**
     * 도메인 로직에서 발생한 커스텀 예외(DomainException)를 처리
     * 예: 서비스 계층 등에서 비즈니스 규칙 위반 또는 잘못된 상태에 대해 명시적으로 throw 된 도메인 예외
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponseDto> handleDomainException(DomainException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity.status(errorCode.httpStatus())
                .body(ErrorResponseDto.toDto(errorCode));
    }
}
