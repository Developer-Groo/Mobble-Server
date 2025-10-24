package com.mobble.mobbleserver.application.article.port.required;

import com.mobble.mobbleserver.domain.article.Article;

public interface ArticleWritePort {

    Article save(Article article);

    void delete(Article article);
}
