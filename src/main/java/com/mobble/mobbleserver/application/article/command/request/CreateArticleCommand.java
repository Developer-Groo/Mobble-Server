package com.mobble.mobbleserver.application.article.command.request;

import com.mobble.mobbleserver.domain.article.ArticleType;

public record CreateArticleCommand(
        Long memberId,
        Long clubId,
        ArticleType type,
        String title,
        String content
) {

    public static CreateArticleCommand create(
            Long memberId,
            Long clubId,
            ArticleType type,
            String title,
            String content
    ) {
        return new CreateArticleCommand(memberId, clubId, type, title, content);
    }
}
