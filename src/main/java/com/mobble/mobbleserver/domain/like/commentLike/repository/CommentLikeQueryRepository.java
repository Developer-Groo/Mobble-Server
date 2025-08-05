package com.mobble.mobbleserver.domain.like.commentLike.repository;

import java.util.List;

public interface CommentLikeQueryRepository {

    void deleteAllByArticleId(Long articleId);

    void deleteAllCommentLikeByArticleIds(List<Long> articleIds);
}
