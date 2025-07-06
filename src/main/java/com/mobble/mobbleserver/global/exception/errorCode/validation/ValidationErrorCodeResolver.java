package com.mobble.mobbleserver.global.exception.errorCode.validation;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.comment.CommentValidationErrorCode;

import java.util.Arrays;

public class ValidationErrorCodeResolver {

    public static String resolve(String key) {
        if (key == null || !key.contains(":")) return "알 수 없는 유효성 검사 오류입니다.";

        String[] parts = key.split(":");
        String domain = parts[0];
        String code = parts[1];

        return switch (domain) {
            case "COMMENT" -> resolveEnum(code, CommentValidationErrorCode.values());
            default -> "알 수 없는 도메인 메세지입니다.";
        };
    }

    private static <E extends Enum<E> & ErrorCode> String resolveEnum(String code, E[] values) {
        return Arrays.stream(values)
                .filter(e -> e.name().equals(code))
                .findFirst()
                .map(ErrorCode::message)
                .orElse("존재하지 않는 ErrorCode 입니다.");
    }
}
