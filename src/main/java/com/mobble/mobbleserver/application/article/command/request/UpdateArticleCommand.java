package com.mobble.mobbleserver.application.article.command.request;

public record UpdateArticleCommand(
        Long memberId,
        Long clubId,
        Long articleId,
        String title,
        String content
) {

    public static UpdateArticleCommand create(
            Long memberId,
            Long clubId,
            Long articleId,
            String title,
            String content
    ) {
        return new UpdateArticleCommand(memberId, clubId, articleId, title, content);
    }
}
