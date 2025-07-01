package com.mobble.mobbleserver.domain.comment.dto.response;

import com.mobble.mobbleserver.domain.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public record CommentListResponseDto(
        Long commentId,
        Long memberId,
        String name,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ReplyCommentDto> replies
) {

    public static CommentListResponseDto toDto(Comment comment) {
        return new CommentListResponseDto(
                comment.getId(),
//            Todo: 필드 구현 완료되면 작업 진행
//            comment.getArticle().getId(),
                comment.getMember().getId(),
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
