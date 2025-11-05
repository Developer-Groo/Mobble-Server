package com.mobble.mobbleserver.shared.error;

public enum ErrorCategory {
    VALIDATION,           // 입력/검증 위반 (DTO, Domain 값 검증)
    NOT_FOUND,            // 리소스 없음
    PERMISSION_DENIED,    // 권한 없음
    CONFLICT,             // 중복/상태 충돌
    UNAVAILABLE,          // 일시적 불가(외부 연등 등)
    INTERNAL_SERVER_ERROR // 예상치 못한 내부 오류
}
