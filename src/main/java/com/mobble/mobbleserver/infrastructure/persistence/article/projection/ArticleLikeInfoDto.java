package com.mobble.mobbleserver.infrastructure.persistence.article.projection;

public record ArticleLikeInfoDto(int likeCount, boolean isLiked) {

    public static ArticleLikeInfoDto toDto(int likeCount, boolean isLiked) {
        return new ArticleLikeInfoDto(likeCount, isLiked);
    }
}
