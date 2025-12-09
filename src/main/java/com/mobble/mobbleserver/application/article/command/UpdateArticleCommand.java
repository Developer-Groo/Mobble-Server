package com.mobble.mobbleserver.application.article.command;

public record UpdateArticleCommand(
        Long memberId,
        Long clubId,
        Long articleId,
        String title,
        String content,
        Long imageId
) {

    public static UpdateArticleCommand create(
            Long memberId,
            Long clubId,
            Long articleId,
            String title,
            String content,
            Long imageId
    ) {
        return new UpdateArticleCommand(memberId, clubId, articleId, title, content, imageId);
    }
}
