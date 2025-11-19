package com.mobble.mobbleserver.application.comment.command;

public record UpdateCommentCommand(
        Long memberId,
        Long clubId,
        Long articleId,
        Long commentId,
        String content
) {

    public static UpdateCommentCommand create(Long memberId, Long clubId, Long articleId, Long commentId, String content) {
        return new UpdateCommentCommand(memberId, clubId, articleId, commentId, content);
    }
}
