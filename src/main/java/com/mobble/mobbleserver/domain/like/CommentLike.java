package com.mobble.mobbleserver.domain.like;

import com.mobble.mobbleserver.domain.common.exception.DomainException;
import com.mobble.mobbleserver.domain.like.error.LikeError;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike extends AbstractLike {

    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Builder(access = AccessLevel.PRIVATE)
    private CommentLike(Long memberId, Long commentId) {
        super(memberId);
        if (commentId == null) throw new DomainException(LikeError.REQUIRED_COMMENT);
        this.commentId = commentId;
    }

    public static CommentLike createCommentLike(Long memberId, Long commentId) {
        return CommentLike.builder()
                .memberId(memberId)
                .commentId(commentId)
                .build();
    }
}
