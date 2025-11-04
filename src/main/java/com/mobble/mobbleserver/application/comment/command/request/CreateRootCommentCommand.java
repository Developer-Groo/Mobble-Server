package com.mobble.mobbleserver.application.comment.command.request;

public record CreateRootCommentCommand(Long memberId, Long clubId, Long articleId, String content) {

    public static CreateRootCommentCommand create(
            Long memberId,
            Long clubId,
            Long articleId,
            String content
    ) {
        return new CreateRootCommentCommand(memberId, clubId, articleId, content);
    }
}
