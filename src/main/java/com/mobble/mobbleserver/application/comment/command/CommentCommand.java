package com.mobble.mobbleserver.application.comment.command;

public record CommentCommand() {

    public record CreateRootCommentCommand(Long memberId, Long articleId, String content) {

        public static  CreateRootCommentCommand create(Long memberId, Long articleId, String content) {
            return new CreateRootCommentCommand(memberId, articleId, content);
        }
    }

    public record CreateReplyCommentCommand(Long memberId, Long articleId, Long parentId, String content) {

        public static  CreateReplyCommentCommand create(Long memberId, Long articleId, Long parentId , String content) {
            return new CreateReplyCommentCommand(memberId, articleId, parentId, content);
        }
    }

    public record UpdateCommentCommand(Long memberId, Long articleId, Long commentId, String content) {

        public static  UpdateCommentCommand create(Long memberId, Long articleId, Long commentId, String content) {
            return new UpdateCommentCommand(memberId, articleId, commentId, content);
        }
    }
}
