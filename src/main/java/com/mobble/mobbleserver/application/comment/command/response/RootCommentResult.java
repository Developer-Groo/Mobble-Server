package com.mobble.mobbleserver.application.comment.command.response;

import com.mobble.mobbleserver.domain.comment.Comment;

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

    public static List<RootCommentResult> create(List<Comment> comments, Map<Long, Long> likeCounts, List<Long> likedIds) {
        return comments.stream()
                .map(comment -> toRoot(comment, likeCounts, likedIds))
                .toList();
    }

    private static RootCommentResult toRoot(Comment comment, Map<Long, Long> likeCounts, List<Long> isLikedList) {
        return new RootCommentResult(
                comment.getId(),
                comment.getMember().getId(),
                comment.getArticle().getId(),
                comment.getMember().getName(),
                comment.getContent().getBody(),
                likeCounts.getOrDefault(comment.getId(), 0L).intValue(),
                isLikedList.contains(comment.getId()),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getChildren().stream()
                        .map(reply -> ReplyCommentResult.toDto(reply, likeCounts, isLikedList))
                        .toList()
        );
    }
}
