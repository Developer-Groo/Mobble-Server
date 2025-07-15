package com.mobble.mobbleserver.global.exception.handler;

import com.mobble.mobbleserver.global.exception.errorCode.validation.ValidationErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.validation.ValidationErrorCodeResolver;
import com.mobble.mobbleserver.global.exception.handler.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Order(1)
@RestControllerAdvice
public class ValidationExceptionHandler {

    /**
     * @Valid 어노테이션 기반의 필드 검증 실패 시 처리
     * MethodArgumentNotValidException 을 통해 유효성 검증 에러 메시지를 필드별로 추출해 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String errorField = error.getField();
            String rawCode = Objects.requireNonNull(error.getDefaultMessage(), "Validation 의 error message 가 null 입니다.");
            String resolved = ValidationErrorCodeResolver.resolve(rawCode);
            errors.put(errorField, resolved);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.toDto(ValidationErrorCode.FIELD_VALIDATION_ERROR, errors));
    }

    /**
     * @RequestParam (required = true) 이지만 요청에서 해당 파라미터가 누락된 경우 발생
     * MissingServletRequestParameterException 을 통해 유효성 검증 에러 메시지를 필드별로 추출해 응답
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        Map<String, String> errors = Map.of(parameterName, String.format("%s 필수 요청값이 누락되었습니다.", parameterName));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.toDto(ValidationErrorCode.MISSING_PARAMETER, errors));
    }

    /**
     * @RequestParam
     * @PathVariable 등에서 전달된 값이 기대 타입과 일치하지 않을 경우 발생
     * MethodArgumentTypeMismatchException 을 통해 유효성 검증 에러 메시지를 필드별로 추출해 응답
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String field = ex.getName();
        String message = String.format("'%s' 파라미터 타입이 잘못되었습니다.", field);
        Map<String, String> errors = Map.of(field, message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.toDto(ValidationErrorCode.TYPE_MISMATCH, errors));
    }

    /**
     * @RequestParam
     * @PathVariable 등 파라미터 바인딩 검증 실패 시 처리
     * ConstraintViolationException 을 통해 유효성 검증 에러 메시지를 필드별로 추출해 응답
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex) {
        HashMap<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(cv -> {
            String field = extractFieldName(cv.getPropertyPath());
            String message = String.format("요청 URL 의 %s 값이 유효하지 않습니다.", field);
            errors.put(field, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.toDto(ValidationErrorCode.BAD_REQUEST, errors));
    }

    private String extractFieldName(Path path) {
        String fullPath = path.toString();
        return fullPath.contains(".")
                ? fullPath.substring(fullPath.lastIndexOf('.') + 1)
                : fullPath;
    }
}
