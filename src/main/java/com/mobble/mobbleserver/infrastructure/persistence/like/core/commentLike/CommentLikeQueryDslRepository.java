package com.mobble.mobbleserver.infrastructure.persistence.like.core.commentLike;

public interface CommentLikeQueryDslRepository {

    void deleteAllByArticleId(Long articleId);
}
