package com.mobble.mobbleserver.domain.commentLike.entity;

import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.member.entity.Member;
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
    private Comment commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @Builder
    public CommentLike(Comment commentId, Member memberId) {
        this.commentId = commentId;
        this.memberId = memberId;
    }
}
