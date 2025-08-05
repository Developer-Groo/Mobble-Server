package com.mobble.mobbleserver.domain.like.commentLike.repository;

import java.util.List;

public interface CommentLikeQueryDslRepository {

    void deleteAllByArticleIds(List<Long> articleIds);
}
