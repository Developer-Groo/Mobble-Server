package com.mobble.mobbleserver.application.comment.port.required;

import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentReadPort {

    Optional<Comment> findById(Long id);

    Optional<Comment> findByIdAndArticleId(Long commentId, Long articleId);

    List<Comment> findCommentsWithRepliesByArticleId(Long articleId);

    List<Long> findIdsByArticleId(Long articleId);

    List<Long> findIdsByArticleIdIn(List<Long> articleIds);

    Map<Long, Integer> countCommentsByArticleIds(List<Long> articleIds);
}
