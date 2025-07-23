package com.mobble.mobbleserver.global.exception.errorCode.article;

import com.mobble.mobbleserver.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor

public enum ArticleErrorCode implements ErrorCode {
    CLUB_REQUIRED("아티클이 속한 클럽은 필수입니다.", HttpStatus.BAD_REQUEST),
    MEMBER_REQUIRED("아티클 작성자는 필수입니다.", HttpStatus.BAD_REQUEST),
    TYPE_REQUIRED("아티클 타입은 필수입니다.", HttpStatus.BAD_REQUEST),
    TITLE_REQUIRED("아티클 제목은 필수입니다.", HttpStatus.BAD_REQUEST),
    CONTENT_REQUIRED("아티클 내용은 필수입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND("아티클을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOTICE_NO_PERMISSION("일반 클럽 회원은 공지글을 작성 및 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NO_PERMISSION("해당 아티클을 수정 또는 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);




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
