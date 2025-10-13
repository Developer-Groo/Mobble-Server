package com.mobble.mobbleserver.refactor.like.commentLike.repository;

public interface CommentLikeQueryRepository {

    void deleteAllByArticleId(Long articleId);
}
