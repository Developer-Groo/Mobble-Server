package com.mobble.mobbleserver.domain.like.commentLike.dto.response;

public record CommentLikeResponseDto(Long commentId, boolean isLiked) {

    public static CommentLikeResponseDto toDto(Long commentId, boolean isLiked) {
        return new CommentLikeResponseDto(commentId, isLiked);
    }
}
