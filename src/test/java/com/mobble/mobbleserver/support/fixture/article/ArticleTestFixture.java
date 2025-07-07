package com.mobble.mobbleserver.support.fixture.article;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;

public class ArticleTestFixture {

    public static Article createDefaultArticle() {
        return Article.createArticle(ArticleType.FREE, "title", "content");
    }
}
