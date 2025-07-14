package com.mobble.mobbleserver.domain.article.repository;

import com.mobble.mobbleserver.domain.article.dto.response.ArticleSummaryResponseDto;

import java.util.List;

public interface ArticleQueryDslRepository {
    List<ArticleSummaryResponseDto> findArticlesByClubId(Long clubId);
}
