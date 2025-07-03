package com.mobble.mobbleserver.like.articleLike.dto.response;

import com.mobble.mobbleserver.like.LikeType;

public record ArticleLikeToggleResponseDto(
        LikeType targetType,
        Long articleId,
        Long memberId,
        String status // "LIKED" or "UNLIKED"
) {
    public static ArticleLikeToggleResponseDto toDto(boolean isLiked, Long articleId, Long memberId) {
        return new ArticleLikeToggleResponseDto(
                LikeType.ARTICLE,
                articleId,
                memberId,
                isLiked ? "LIKED" : "UNLIKED"
        );
    }
}