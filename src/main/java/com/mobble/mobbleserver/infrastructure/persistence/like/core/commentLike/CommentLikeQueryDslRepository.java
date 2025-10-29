package com.mobble.mobbleserver.infrastructure.persistence.like.core.commentLike;

import java.util.List;

public interface CommentLikeQueryDslRepository {

    void deleteAllByArticleId(Long articleId);

    void deleteAllByArticleIds(List<Long> articleIds);
}
