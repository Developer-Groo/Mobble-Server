package com.mobble.mobbleserver.application.article.port.provided;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.infrastructure.web.article.dto.request.ArticleRequestDto;

public interface ArticleUpdatePort {

    Article updateArticle(Long articleId, Long memberId, ArticleRequestDto dto);
}
