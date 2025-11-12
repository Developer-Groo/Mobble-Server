package com.mobble.mobbleserver.application.comment.command.response;

import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record RootCommentResult(
        Long commentId,
        Long memberId,
        Long articleId,
        String name,
        String content,
        int likeCount,
        boolean isLiked,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ReplyCommentResult> replies
) {

    public static RootCommentResult toDto(Comment comment, Map<Long, CommentLikeInfoDto> likeInfoMap) {
        CommentLikeInfoDto info = likeInfoMap.getOrDefault(comment.getId(), new CommentLikeInfoDto(0, false));

        return new RootCommentResult(
                comment.getId(),
                comment.getMember().getId(),
                comment.getArticle().getId(),
                comment.getMember().getName(),
                comment.getContent().getBody(),
                info.likeCount(),
                info.isLiked(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getChildren().stream()
                        .map(reply -> ReplyCommentResult.toDto(reply, likeInfoMap))
                        .toList()
        );
    }
}
