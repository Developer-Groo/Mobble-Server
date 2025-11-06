package com.mobble.mobbleserver.application.article.port.provided;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.infrastructure.web.article.dto.request.ArticleRequestDto;

public interface ArticleCreatePort {

    Article createArticle(Long memberId, Long clubId, ArticleRequestDto dto);
}
