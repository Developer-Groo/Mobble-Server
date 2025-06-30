package com.mobble.mobbleserver.domain.comment.dto.request;

import com.mobble.mobbleserver.domain.comment.entity.Comment;

public record CommentRequestDto(Comment parent, String content) {

    public Comment toEntity() {
        if (parent == null) {
            return Comment.createRootComment(this.content);
        }
        return Comment.createReplyComment(this.parent, this.content);
    }
}
// Todo: 필요한 필드 구현 완료 되면 작업