package com.mobble.mobbleserver.domain.like;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike extends AbstractLike {

    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Builder(access = AccessLevel.PRIVATE)
    private CommentLike(Long memberId, Long commentId) {
        super(memberId);
        requireNonNull(commentId, "commentId must not be null");
        this.commentId = commentId;
    }

    public static CommentLike create(Long memberId, Long commentId) {
        return CommentLike.builder()
                .memberId(memberId)
                .commentId(commentId)
                .build();
    }
}
