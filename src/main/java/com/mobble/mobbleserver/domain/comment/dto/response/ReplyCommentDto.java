package com.mobble.mobbleserver.domain.comment.dto.response;

import com.mobble.mobbleserver.domain.comment.entity.Comment;

import java.time.LocalDateTime;

public record ReplyCommentDto(
        Long commentId,
        Long memberId,
        String name,
        Long parentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ReplyCommentDto toDto(Comment comment) {
        return new ReplyCommentDto(
                comment.getId(),
                comment.getMember().getId(),
                comment.getMember().getName(),
                Boolean.TRUE.equals(comment.hasParent()) ? comment.getParent().getId() : null,
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
