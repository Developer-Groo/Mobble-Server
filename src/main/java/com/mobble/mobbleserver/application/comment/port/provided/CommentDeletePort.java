package com.mobble.mobbleserver.application.comment.port.provided;

public interface CommentDeletePort {

    void deleteComment(Long memberId, Long clubId, Long articleId, Long commentId);
}
