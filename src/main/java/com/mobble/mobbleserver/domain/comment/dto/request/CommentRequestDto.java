package com.mobble.mobbleserver.domain.comment.dto.request;

import com.mobble.mobbleserver.domain.comment.entity.Comment;

public record CommentRequestDto(String content) {

    public Comment toEntity() { // Todo: article, memeber 객체 필요
        return Comment.createRootComment(this.content);
    }

    public Comment toEntity(Comment parent) { // Todo: article, memeber 객체 필요
        return Comment.createReplyComment(parent, this.content);
    }
}
