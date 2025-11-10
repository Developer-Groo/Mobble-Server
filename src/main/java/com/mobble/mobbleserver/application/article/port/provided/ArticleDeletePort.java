package com.mobble.mobbleserver.application.article.port.provided;

public interface ArticleDeletePort {

    void delete(Long clubId, Long articleId, Long memberId);

    void deleteAll(Long clubId);
}
