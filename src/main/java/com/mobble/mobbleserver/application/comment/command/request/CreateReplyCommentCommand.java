package com.mobble.mobbleserver.application.comment.command.request;

public record CreateReplyCommentCommand(Long memberId, Long clubId, Long articleId, Long parentId, String content) {

    public static CreateReplyCommentCommand create(
            Long memberId,
            Long clubId,
            Long articleId,
            Long parentId,
            String content
    ) {
        return new CreateReplyCommentCommand(memberId, clubId, articleId, parentId, content);
    }
}
