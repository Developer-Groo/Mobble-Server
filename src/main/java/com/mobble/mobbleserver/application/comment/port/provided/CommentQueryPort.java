package com.mobble.mobbleserver.application.comment.port.provided;

import com.mobble.mobbleserver.infrastructure.web.comment.dto.response.RootCommentResponseDto;

import java.util.List;

public interface CommentQueryPort {

    List<RootCommentResponseDto> getCommentListByArticle(Long articleId, Long memberId);
}
