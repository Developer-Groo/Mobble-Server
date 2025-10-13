package com.mobble.mobbleserver.application.comment.port.provided;

import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.web.comment.dto.request.CommentRequestDto;

public interface CommentUpdatePort {

    Comment updateComment(
            Long articleId,
            Long commentId,
            Long memberId,
            CommentRequestDto dto
    );
}
