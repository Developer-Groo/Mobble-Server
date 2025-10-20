package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

public interface CommentLikeQueryRepository {

    void deleteAllByArticleId(Long articleId);
}
