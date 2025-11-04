package com.mobble.mobbleserver.infrastructure.web.exception;

import com.mobble.mobbleserver.shared.error.ErrorCategory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HttpStatusMapper {

    private static final Map<ErrorCategory, HttpStatus> MAP = Map.of(
            ErrorCategory.VALIDATION, HttpStatus.BAD_REQUEST,
            ErrorCategory.NOT_FOUND, HttpStatus.NOT_FOUND,
            ErrorCategory.PERMISSION_DENIED, HttpStatus.FORBIDDEN,
            ErrorCategory.CONFLICT, HttpStatus.CONFLICT,
            ErrorCategory.UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE,
            ErrorCategory.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR
    );

    public HttpStatus toStatus(ErrorCategory category) {
        return MAP.getOrDefault(category, HttpStatus.BAD_REQUEST);
    }
}
