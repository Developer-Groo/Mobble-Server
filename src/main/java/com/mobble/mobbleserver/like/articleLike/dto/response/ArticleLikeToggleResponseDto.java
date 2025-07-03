package com.mobble.mobbleserver.like.articleLike.dto.response;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.like.LikeType;

public record ArticleLikeToggleResponseDto(
        LikeType targetType,
        Long articleId,
        Long memberId,
        String status // "LIKED" or "UNLIKED"
) {
    public static ArticleLikeToggleResponseDto toDto(boolean isLiked, Article articleId, Member memberId) {
        return new ArticleLikeToggleResponseDto(
                LikeType.ARTICLE,
                articleId.getId(),
                memberId.getId(),
                isLiked ? "LIKED" : "UNLIKED"
        );
    }
}