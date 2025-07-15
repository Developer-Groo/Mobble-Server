package com.mobble.mobbleserver.global.exception.errorCode.like;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum LikeErrorCode implements ErrorCode {
    MEMBER_REQUIRED("로그인 후 이용할 수 있습니다.", HttpStatus.BAD_REQUEST),
    CLUB_REQUIRED("좋아요 할 클럽은 필수입니다.", HttpStatus.BAD_REQUEST),
    ARTICLE_REQUIRED("좋아요 할 게시글은 필수입니다.", HttpStatus.BAD_REQUEST),
    COMMENT_REQUIRED("좋아요 할 댓글은 필수입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public String message() {
        return message();
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus();
    }
}
