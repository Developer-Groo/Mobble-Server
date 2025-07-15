package com.mobble.mobbleserver.domain.article.repository;

import com.mobble.mobbleserver.domain.article.dto.response.ArticleDetailDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;

import java.util.List;

public interface ArticleQueryDslRepository {
    List<ArticleSummaryResponseDto> findArticlesByClubId(Long clubId, ArticleType articleType);

    ArticleDetailDto findArticleDetailById(Long articleId);
}
