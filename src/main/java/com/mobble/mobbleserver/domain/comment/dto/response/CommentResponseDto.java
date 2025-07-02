package com.mobble.mobbleserver.domain.comment.dto.response;

import com.mobble.mobbleserver.domain.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long commentId,
        Long memberId,
        Long articleId,
        String name,
        Long parentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getMember().getId(),
                comment.getArticle().getId(),
                comment.getMember().getName(),
                Boolean.TRUE.equals(comment.hasParent()) ? comment.getParent().getId() : null,
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
