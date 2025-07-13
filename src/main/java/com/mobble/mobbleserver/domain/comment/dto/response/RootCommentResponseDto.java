package com.mobble.mobbleserver.domain.comment.dto.response;

import com.mobble.mobbleserver.domain.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public record RootCommentResponseDto(
        Long commentId,
        Long memberId,
        Long articleId,
        String name,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ReplyCommentDto> replies
) {

    public static RootCommentResponseDto toDto(Comment comment) {
        return new RootCommentResponseDto(
                comment.getId(),
                comment.getMember().getId(),
                comment.getArticle().getId(),
                comment.getMember().getName(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getChildren().stream()
                        .map(ReplyCommentDto::toDto)
                        .toList()
        );
    }
}
