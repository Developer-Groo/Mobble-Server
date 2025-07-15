package com.mobble.mobbleserver.domain.comment.repository.dto;

public record CommentLikeInfoDto(int likeCount, boolean isLiked) {

    public static CommentLikeInfoDto toDto(int likeCount, boolean isLiked) {
        return new CommentLikeInfoDto(likeCount, isLiked);
    }
}
