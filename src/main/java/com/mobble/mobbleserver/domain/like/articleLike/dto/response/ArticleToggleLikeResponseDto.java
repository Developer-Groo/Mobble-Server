package com.mobble.mobbleserver.domain.like.articleLike.dto.response;

public record ArticleToggleLikeResponseDto(Long articleId, boolean isLiked) {

    public static ArticleToggleLikeResponseDto toDto(Long articleId, boolean isLiked) {
        return new ArticleToggleLikeResponseDto(articleId, isLiked);
    }
}
