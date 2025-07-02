package com.mobble.mobbleserver.domain.comment.dto.request;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.member.entity.Member;

public record CommentRequestDto(String content) {

    public Comment toEntity(Member member, Article article) {
        return Comment.createRootComment(member, article, this.content);
    }

    public Comment toEntity(Member member, Article article, Comment parent) {
        return Comment.createReplyComment(member, article, parent, this.content);
    }
}
