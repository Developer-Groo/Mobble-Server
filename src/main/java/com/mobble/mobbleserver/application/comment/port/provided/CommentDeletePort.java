package com.mobble.mobbleserver.application.comment.port.provided;

import java.util.List;

public interface CommentDeletePort {

    void deleteComment(Long memberId, Long clubId, Long articleId, Long commentId);

    void deleteAllComment(Long clubId, Long articleId);

    void deleteAllCommentByArticleIds(List<Long> articleIds);
}
