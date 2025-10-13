package com.mobble.mobbleserver.refactor.article.repository;

import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.entity.ArticleType;
import com.mobble.mobbleserver.refactor.article.repository.dto.ArticleLikeInfoDto;

import java.util.List;
import java.util.Map;

public interface ArticleQueryRepository {

    List<Article> findArticlesByClubId(Long clubId, ArticleType articleType);

    public Map<Long, ArticleLikeInfoDto> findLikeInfoByArticleIdsAndMemberId(List<Long> articleIds, Long memberId);

    List<Long> findArticleIdsByClubId(Long clubId);
}
