package com.mobble.mobbleserver.domain.comment.dto.response;

import com.mobble.mobbleserver.domain.comment.repository.dto.CommentLikeInfoDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;

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
                comment.getContent(),
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
