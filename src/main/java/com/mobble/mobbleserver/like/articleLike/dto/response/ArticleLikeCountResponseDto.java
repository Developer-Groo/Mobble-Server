package com.mobble.mobbleserver.like.articleLike.dto.response;

public record ArticleLikeCountResponseDto(Long articleId, int likeCount) {
    public static ArticleLikeCountResponseDto toDto(Long articleId, int count) {
        return new ArticleLikeCountResponseDto(articleId, count);
    }
}
