package com.mobble.mobbleserver.application.article.port.provided;

import com.mobble.mobbleserver.application.article.command.response.ArticleDetailResult;
import com.mobble.mobbleserver.application.article.command.response.ArticlePreviewResult;
import com.mobble.mobbleserver.domain.article.ArticleType;

import java.util.List;

public interface ArticleQueryPort {

    List<ArticlePreviewResult> getArticlesPreview(Long clubId, Long memberId, ArticleType articleType);

    ArticleDetailResult getArticleDetail(Long clubId, Long articleId, Long memberId);
}
