package com.mobble.mobbleserver.application.comment.result;

import com.mobble.mobbleserver.domain.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ReplyCommentResult(
        Long commentId,
        Long memberId,
        String name,
        Long parentId,
        String content,
        int likeCount,
        boolean isLiked,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ReplyCommentResult toDto(Comment comment, Map<Long, Integer> likeCounts, List<Long> isLikedList) {
        return new ReplyCommentResult(
                comment.getId(),
                comment.getMember().getId(),
                comment.getMember().getName(),
                comment.hasParent() ? comment.getParent().getId() : null,
                comment.getContent().getBody(),
                likeCounts.getOrDefault(comment.getId(), 0),
                isLikedList.contains(comment.getId()),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
