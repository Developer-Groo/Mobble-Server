package com.mobble.mobbleserver.domain.like.articleLike.dto.response;

public record ArticleLikeCountResponseDto(Long articleId, int likeCount) {
    public static ArticleLikeCountResponseDto toDto(Long articleId, int likeCount) {
        return new ArticleLikeCountResponseDto(articleId, likeCount);
    }
}
