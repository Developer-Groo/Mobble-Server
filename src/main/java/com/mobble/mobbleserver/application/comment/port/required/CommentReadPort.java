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

    Map<Long, CommentLikeInfoDto> findLikeInfoByCommentIdsAndMemberId(List<Long> ids, Long memberId);

    Map<Long, Integer> countCommentsByArticleIds(List<Long> articleIds);

    void deleteAllCommentByArticle_IdIn(List<Long> articleIds);
}
