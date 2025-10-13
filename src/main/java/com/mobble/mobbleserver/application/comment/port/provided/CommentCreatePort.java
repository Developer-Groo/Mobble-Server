package com.mobble.mobbleserver.application.comment.port.provided;

import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.web.comment.dto.request.CommentRequestDto;

public interface CommentCreatePort {

    Comment createRootComment(Long memberId, Long articleId, CommentRequestDto dto);

    Comment createReplyComment(
            Long memberId,
            Long articleId,
            Long parentCommentId,
            CommentRequestDto dto
    );
}
