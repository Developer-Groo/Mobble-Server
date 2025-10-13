package com.mobble.mobbleserver.infrastructure.persistence.comment.projection;

public record CommentLikeInfoDto(int likeCount, boolean isLiked) {

    public static CommentLikeInfoDto toDto(int likeCount, boolean isLiked) {
        return new CommentLikeInfoDto(likeCount, isLiked);
    }
}
