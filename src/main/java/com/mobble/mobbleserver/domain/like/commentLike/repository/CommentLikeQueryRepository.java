package com.mobble.mobbleserver.domain.like.commentLike.repository;

public interface CommentLikeQueryRepository {

    void deleteAllByArticleId(Long articleId);
}
