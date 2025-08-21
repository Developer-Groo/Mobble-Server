package com.mobble.mobbleserver.domain.like.commentLike.entity;

import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.like.entity.BaseLike;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "comment_like_id"))
public class CommentLike extends BaseLike {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Builder(access = AccessLevel.PRIVATE)
    private CommentLike(Comment comment, Member member) {
        if (comment == null) throw new DomainException(LikeErrorCode.COMMENT_REQUIRED);
        this.comment = comment;
        assignMember(member);
    }

    public static CommentLike createCommentLike(Comment comment, Member member) {
        return CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();
    }
}
