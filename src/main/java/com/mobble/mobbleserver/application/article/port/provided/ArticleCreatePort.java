package com.mobble.mobbleserver.application.article.port.provided;

import com.mobble.mobbleserver.application.article.command.request.CreateArticleCommand;
import com.mobble.mobbleserver.domain.article.Article;

public interface ArticleCreatePort {

    Article createArticle(CreateArticleCommand command);
}
