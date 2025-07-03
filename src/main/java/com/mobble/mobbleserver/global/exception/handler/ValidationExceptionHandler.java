package com.mobble.mobbleserver.global.exception.handler;

import com.mobble.mobbleserver.global.exception.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class ValidationExceptionHandler {

    private final MessageSource messageSource;

    /**
     * @Valid 어노테이션 기반의 필드 검증 실패 시 처리
     * MethodArgumentNotValidException 을 통해 유효성 검증 에러 메시지를 필드별로 추출해 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String errorField = error.getField();
            String message = error.getDefaultMessage();
            errors.put(errorField, message);
        });

        String message = messageSource.getMessage("error.validation", null, locale);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.toDto(message, errors));
    }

    /**
     * @PathVariable 등 컨트롤러 메서드 파라미터 검증 실패 시 처리
     * ConstraintViolationException 을 통해 유효성 검증 에러 메시지를 필드별로 추출해 응답
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex, Locale locale) {
        HashMap<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(cv -> {
            String field = extractFieldName(cv.getPropertyPath());
            String message = messageSource.getMessage("error.pathVariable.invalid", new Object[]{field}, locale);
            errors.put(field, message);
        });

        String message = messageSource.getMessage("error.bad_request", null, locale);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.toDto(message, errors));
    }

    private String extractFieldName(Path path) {
        String fullPath = path.toString();
        return fullPath.contains(".")
                ? fullPath.substring(fullPath.lastIndexOf('.') + 1)
                : fullPath;
    }
}
