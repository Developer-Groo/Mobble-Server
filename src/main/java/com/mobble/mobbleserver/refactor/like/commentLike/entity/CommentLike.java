package com.mobble.mobbleserver.refactor.like.commentLike.entity;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.refactor.like.baseLike.entity.AbstractLike;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike extends AbstractLike {

    @JoinColumn(name = "comment_id", nullable = false)
    private Long commentId;

    @Builder(access = AccessLevel.PRIVATE)
    private CommentLike(Long memberId, Long commentId) {
        super(memberId);
        if (commentId == null) throw new DomainException(LikeErrorCode.COMMENT_REQUIRED);
        this.commentId = commentId;
    }

    public static CommentLike createCommentLike(Long memberId, Long commentId) {
        return CommentLike.builder()
                .commentId(commentId)
                .memberId(memberId)
                .build();
    }
}
