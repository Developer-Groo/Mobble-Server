package com.mobble.mobbleserver.domain.like.commentLike.entity;

import com.mobble.mobbleserver.domain.comment.entity.Comment;
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
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private CommentLike(Comment comment, Member member) {
        this.comment = comment;
        this.member = member;
    }

    public static CommentLike createcommentLike(Comment comment, Member member) {
        if (comment == null) throw new DomainException(LikeErrorCode.COMMENT_REQUIRED);
        if (member == null) throw new DomainException(LikeErrorCode.MEMBER_REQUIRED);

        return CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();
    }
}
