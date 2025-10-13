package com.mobble.mobbleserver.application.comment.port.provided;

public interface CommentDeletePort {

    void deleteComment(Long articleId, Long commentId, Long memberId);
}
