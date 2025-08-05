package com.mobble.mobbleserver.domain.like.articleLike.repository;

import java.util.List;

public interface ArticleLikeQueryDslRepository {

    void deleteAllByArticleIds(List<Long> articleIds);
}
