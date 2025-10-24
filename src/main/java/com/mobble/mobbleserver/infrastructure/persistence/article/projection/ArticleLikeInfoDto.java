package com.mobble.mobbleserver.refactor.article.repository.dto;

public record ArticleLikeInfoDto(int likeCount, boolean isLiked) {

    public static ArticleLikeInfoDto toDto(int likeCount, boolean isLiked) {
        return new ArticleLikeInfoDto(likeCount, isLiked);
    }
}
