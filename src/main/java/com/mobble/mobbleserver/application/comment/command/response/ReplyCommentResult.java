package com.mobble.mobbleserver.infrastructure.web.comment.dto.response;

import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;

import java.time.LocalDateTime;
import java.util.Map;

public record ReplyCommentResponseDto(
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

    public static ReplyCommentResponseDto toDto(Comment comment, Map<Long, CommentLikeInfoDto> likeInfoMap) {
        CommentLikeInfoDto info = likeInfoMap.getOrDefault(comment.getId(), new CommentLikeInfoDto(0, false));

        return new ReplyCommentResponseDto(
                comment.getId(),
                comment.getMember().getId(),
                comment.getMember().getName(),
                comment.hasParent() ? comment.getParent().getId() : null,
                comment.getBody().getContent(),
                info.likeCount(),
                info.isLiked(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
