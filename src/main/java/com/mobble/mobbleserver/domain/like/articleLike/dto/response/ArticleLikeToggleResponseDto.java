package com.mobble.mobbleserver.domain.like.articleLike.dto.response;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.like.LikeType;

public record ArticleLikeToggleResponseDto(
        LikeType targetType,
        Long article,
        Long member,
        String status // "LIKED" or "UNLIKED"
) {
    public static ArticleLikeToggleResponseDto toDto(Article article, Member member, boolean isLiked) {
        return new ArticleLikeToggleResponseDto(
                LikeType.ARTICLE,
                article.getId(),
                member.getId(),
                isLiked ? "LIKED" : "UNLIKED"
        );
    }
}