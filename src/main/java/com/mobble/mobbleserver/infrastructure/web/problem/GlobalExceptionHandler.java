package com.mobble.mobbleserver.infrastructure.web.problem;

import com.mobble.mobbleserver.util.DateTimeUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@Order(3)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 인가되지 않은 사용자가 권한 검증이 필요한 리소스에 접근하려고 할 때 처리
     * 예: 일반 회원(MEMBER)이 관리자 전용(LEADER, MANAGER) 권한 API에 접근할 경우
     * #   @PreAuthorize 조건이 false를 반환한 경우
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        return toProblemDetail(HttpStatus.FORBIDDEN, ex, "SECURITY.ACCESS_DENIED");
    }

    /**
     * 예상하지 못한 모든 예외(Exception)를 처리
     * 예: 위에서 처리되지 않은 런타임 예외 or 시스템 오류 등이 발생한 경우
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleInternalServerError(Exception ex, HttpServletRequest request) {
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

        return toProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex, "GLOBAL.INTERNAL_SERVER_ERROR");
    }

    private ProblemDetail toProblemDetail(HttpStatus status, Exception ex, String title) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        detail.setTitle(title);
        detail.setProperty("timestamp", DateTimeUtils.now());
        detail.setProperty("problem", ex.getClass().getName());

        return detail;
    }
}
