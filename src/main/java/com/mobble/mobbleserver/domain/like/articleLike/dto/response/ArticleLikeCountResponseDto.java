package com.mobble.mobbleserver.domain.like.articleLike.dto.response;

import com.mobble.mobbleserver.domain.article.entity.Article;

public record ArticleLikeCountResponseDto(Long article, int likeCount) {
    public static ArticleLikeCountResponseDto toDto(Article article, int likeCount) {
        return new ArticleLikeCountResponseDto(article.getId(), likeCount);
    }
}
