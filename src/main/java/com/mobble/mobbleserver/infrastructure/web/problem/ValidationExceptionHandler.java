package com.mobble.mobbleserver.infrastructure.web.problem;

import com.mobble.mobbleserver.util.DateTimeUtils;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;

@Order(1)
@RestControllerAdvice
public class ValidationExceptionHandler {

    /**
     * @Valid 어노테이션 기반의 필드 검증 실패 시 처리
     * MethodArgumentNotValidException 을 통해 유효성 검증 에러 메시지를 필드별로 추출해 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "code", String.valueOf(error.getCode()),
                        "message", String.valueOf(error.getDefaultMessage())
                ))
                .toList();

        return toProblemDetail(fieldErrors, ex);
    }

    /**
     * @RequestParam (required = true) 이지만 요청에서 해당 파라미터가 누락된 경우 발생
     * MissingServletRequestParameterException 을 통해 유효성 검증 에러 메시지를 필드별로 추출해 응답
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingRequestParam(MissingServletRequestParameterException ex) {
        List<Map<String, String>> fieldErrors = List.of(Map.of(
                "field", ex.getParameterName(),
                "code", "MissingParameter",
                "message", "Missing required parameter: " + ex.getParameterName()
        ));

        return toProblemDetail(fieldErrors, ex);
    }

    /**
     * @RequestParam/@PathVariable 등에서 전달된 값이 기대 타입과 일치하지 않을 경우 발생
     * MethodArgumentTypeMismatchException 을 통해 유효성 검증 에러 메시지를 필드별로 추출해 응답
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String required = (ex.getRequiredType() != null) ? ex.getRequiredType().getSimpleName() : "unknown";
        List<Map<String, String>> fieldErrors = List.of(Map.of(
                "field", ex.getName(),
                "code", "TypeMismatch",
                "message", "Invalid type for parameter: " + ex.getName() + ", Expected: " + required
        ));

        return toProblemDetail(fieldErrors, ex);
    }

    /**
     * @RequestParam/@PathVariable 등 파라미터 바인딩 검증 실패 시 처리
     * ConstraintViolationException 을 통해 유효성 검증 에러 메시지를 필드별로 추출해 응답
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {

        List<Map<String, String>> violations = ex.getConstraintViolations()
                .stream()
                .map(v -> Map.of(
                        "property", v.getPropertyPath().toString(),
                        "message", v.getMessage()
                ))
                .toList();

        return toProblemDetail(violations, ex);
    }

    private ProblemDetail toProblemDetail(List<Map<String, String>> fieldErrors, Exception ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        detail.setTitle("VALIDATION_ERROR");

        detail.setProperty("errors", fieldErrors);
        detail.setProperty("timestamp", DateTimeUtils.now());

        return detail;
    }
}
