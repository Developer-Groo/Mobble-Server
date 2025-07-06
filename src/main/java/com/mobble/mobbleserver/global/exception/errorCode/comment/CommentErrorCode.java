package com.mobble.mobbleserver.global.exception.errorCode.comment;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    MEMBER_REQUIRED("댓글 작성자는 필수입니다.", HttpStatus.BAD_REQUEST),
    ARTICLE_REQUIRED("댓글이 속한 게시글은 필수입니다.", HttpStatus.BAD_REQUEST),
    CONTENT_REQUIRED("댓글 내용은 필수입니다.", HttpStatus.BAD_REQUEST),
    PARENT_REQUIRED("대댓글의 부모 댓글은 필수입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND("댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NO_PERMISSION("해당 댓글을 수정 또는 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);

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
