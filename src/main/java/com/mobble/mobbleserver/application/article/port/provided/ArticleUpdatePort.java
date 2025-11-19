package com.mobble.mobbleserver.application.article.port.provided;

import com.mobble.mobbleserver.application.article.command.UpdateArticleCommand;
import com.mobble.mobbleserver.domain.article.Article;

public interface ArticleUpdatePort {

    Article update(UpdateArticleCommand command);
}
