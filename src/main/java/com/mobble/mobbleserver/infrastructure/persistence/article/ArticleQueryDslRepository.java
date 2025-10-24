package com.mobble.mobbleserver.infrastructure.persistence.article;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.infrastructure.persistence.article.projection.ArticleLikeInfoDto;

import java.util.List;
import java.util.Map;

public interface ArticleQueryDslRepository {

    List<Article> findArticlesByClubId(Long clubId, ArticleType articleType);

    public Map<Long, ArticleLikeInfoDto> findLikeInfoByArticleIdsAndMemberId(List<Long> articleIds, Long memberId);

    List<Long> findArticleIdsByClubId(Long clubId);
}
