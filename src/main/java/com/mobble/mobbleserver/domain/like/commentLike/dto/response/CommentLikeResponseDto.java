package com.mobble.mobbleserver.domain.like.commentLike.dto.response;

public record CommentLikeResponseDto(Long commentId, boolean isLiked, int likeCount) {

    public static CommentLikeResponseDto toDto(Long commentId, boolean isLiked, int likeCount) {
        return new CommentLikeResponseDto(commentId, isLiked, likeCount);
    }
}
