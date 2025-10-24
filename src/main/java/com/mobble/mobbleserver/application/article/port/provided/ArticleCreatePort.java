package com.mobble.mobbleserver.application.article.port.provided;

import com.mobble.mobbleserver.infrastructure.web.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleResponseDto;

public interface ArticleCreatePort {
    ArticleResponseDto createArticle(Long memberId, Long clubId, ArticleRequestDto dto);
}
