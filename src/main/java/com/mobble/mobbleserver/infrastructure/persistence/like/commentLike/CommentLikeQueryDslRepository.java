package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

public interface CommentLikeQueryDslRepository {

    void deleteAllByArticleId(Long articleId);
}
