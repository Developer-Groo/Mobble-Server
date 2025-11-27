package com.mobble.mobbleserver.global.exception.errorCode.oAuth;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum OAuthErrorCode implements ErrorCode {
    INVALID_ACCESS_TOKEN("유효하지 않은 액세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
    FAILED_TO_REQUEST_USER_INFO("소셜 서버에서 사용자 정보를 가져오지 못했습니다.", HttpStatus.BAD_GATEWAY),
    NO_USER_INFO("사용자 정보가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_SOCIAL_PROVIDER("지원하지 않는 SocialProvide 입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
