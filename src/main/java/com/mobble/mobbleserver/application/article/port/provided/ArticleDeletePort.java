package com.mobble.mobbleserver.application.article.port.provided;

public interface ArticleDeletePort {

    void deleteArticle(Long clubId, Long articleId, Long memberId);
}
