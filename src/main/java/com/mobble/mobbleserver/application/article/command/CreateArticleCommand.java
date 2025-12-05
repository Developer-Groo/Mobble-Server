package com.mobble.mobbleserver.application.article.command;

import com.mobble.mobbleserver.domain.article.ArticleType;

public record CreateArticleCommand(
        Long memberId,
        Long clubId,
        ArticleType type,
        String title,
        String content,
        Long imageId
) {

    public static CreateArticleCommand create(
            Long memberId,
            Long clubId,
            ArticleType type,
            String title,
            String content,
            Long imageId
    ) {
        return new CreateArticleCommand(memberId, clubId, type, title, content, imageId);
    }
}
