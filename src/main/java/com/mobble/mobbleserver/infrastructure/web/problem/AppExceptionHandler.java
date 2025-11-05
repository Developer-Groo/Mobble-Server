package com.mobble.mobbleserver.infrastructure.web.problem;

import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.domain.common.exception.DomainException;
import com.mobble.mobbleserver.shared.error.CommonError;
import com.mobble.mobbleserver.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(2)
@RestControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler {

    private final HttpStatusMapper mapper;

    /**
     * 도메인 로직에서 발생한 커스텀 예외(DomainException)를 처리
     * 예: 도메인 계층 등에서 비즈니스 규칙 위반 또는 잘못된 상태에 대해 명시적으로 throw 된 도메인 예외
     */
    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException ex) {
        return toProblemDetail(ex.getError());
    }

    /**
     * 애플리케이션(비즈니스) 계층에서 발생한 커스텀 예외(BusinessException)를 처리
     * 예: 서비스 로직에서 도메인 규칙을 위반하거나 외부 상태(회원, 권한 등)로 인해 수행할 수 없는 요청이 감지될 때 throw 된 비즈니스 예외
     */
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException ex) {
        return toProblemDetail(ex.getError());
    }

    private ProblemDetail toProblemDetail(CommonError error) {
        HttpStatus status = mapper.toStatus(error.category());

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, error.message());
        detail.setTitle(error.code());
        detail.setProperty("errorCode", error.code());
        detail.setProperty("category", error.category().name());
        detail.setProperty("timestamp", DateTimeUtils.now());

        return detail;
    }
}
