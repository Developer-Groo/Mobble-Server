package com.mobble.mobbleserver.domain.comment.dto.response;

import com.mobble.mobbleserver.domain.comment.repository.dto.CommentLikeInfoDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;

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
                comment.getContent(),
                info.likeCount(),
                info.isLiked(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
