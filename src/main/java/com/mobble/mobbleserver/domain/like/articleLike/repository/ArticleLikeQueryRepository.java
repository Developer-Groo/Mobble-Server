package com.mobble.mobbleserver.domain.like.articleLike.repository;

import java.util.List;

public interface ArticleLikeQueryRepository {

    void deleteAllByArticleIds(List<Long> articleIds);
}
