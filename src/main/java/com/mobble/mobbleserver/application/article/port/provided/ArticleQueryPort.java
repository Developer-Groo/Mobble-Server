package com.mobble.mobbleserver.application.article.port.provided;

import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleSummaryResponseDto;

import java.util.List;

public interface ArticleQueryPort {
    List<ArticleSummaryResponseDto> findArticlesByClubId(Long clubId, ArticleType articleType, Long memberId);

    ArticleResponseDto findArticleById(Long articleId, Long memberId);
}
