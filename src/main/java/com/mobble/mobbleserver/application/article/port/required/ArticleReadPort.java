package com.mobble.mobbleserver.application.article.port.required;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.infrastructure.persistence.article.projection.ArticleLikeInfoDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ArticleReadPort {

    Optional<Article> findById(Long id);

    Optional<Article> findByIdAndMemberId(Long articleId, Long memberId);

    boolean existsArticleByIdAndMemberId(Long articleId, Long memberId);

    List<Article> findArticlesByClubId(Long clubId, ArticleType articleType);

    public Map<Long, ArticleLikeInfoDto> findLikeInfoByArticleIdsAndMemberId(List<Long> articleIds, Long memberId);

    List<Long> findArticleIdsByClubId(Long clubId);

}
