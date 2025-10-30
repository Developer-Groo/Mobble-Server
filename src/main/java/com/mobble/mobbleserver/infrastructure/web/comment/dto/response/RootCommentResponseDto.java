package com.mobble.mobbleserver.infrastructure.web.comment.dto.response;

import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record RootCommentResponseDto(
        Long commentId,
        Long memberId,
        Long articleId,
        String name,
        String content,
        int likeCount,
        boolean isLiked,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ReplyCommentResponseDto> replies
) {

    public static RootCommentResponseDto toDto(Comment comment, Map<Long, CommentLikeInfoDto> likeInfoMap) {
        CommentLikeInfoDto info = likeInfoMap.getOrDefault(comment.getId(), new CommentLikeInfoDto(0, false));

        return new RootCommentResponseDto(
                comment.getId(),
                comment.getMember().getId(),
                comment.getArticle().getId(),
                comment.getMember().getName(),
                comment.getBody().getContent(),
                info.likeCount(),
                info.isLiked(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getChildren().stream()
                        .map(reply -> ReplyCommentResponseDto.toDto(reply, likeInfoMap))
                        .toList()
        );
    }
}
