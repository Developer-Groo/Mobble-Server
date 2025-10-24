package com.mobble.mobbleserver.application.article.port.provided;

import com.mobble.mobbleserver.infrastructure.web.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleUpdatedResponseDto;

public interface ArticleUpdatePort {
    ArticleUpdatedResponseDto updateArticle(Long articleId, Long memberId, ArticleRequestDto dto);
}
