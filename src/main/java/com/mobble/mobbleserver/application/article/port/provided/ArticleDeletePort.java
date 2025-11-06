package com.mobble.mobbleserver.application.article.port.provided;

public interface ArticleDeletePort {

    void deleteArticle(Long articleId, Long memberId);
}
