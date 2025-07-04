package com.mobble.mobbleserver.domain.like.articleLike.dto.response;

import com.mobble.mobbleserver.domain.article.entity.Article;

import java.util.List;

public record ArticleLikeCountResponseDto(Long article, int likeCount,
                                          List<ArticleLikeMemberResponseDto> likedMembers) {
    public static ArticleLikeCountResponseDto toDto(Article article, int likeCount, List<ArticleLikeMemberResponseDto> members) {
        return new ArticleLikeCountResponseDto(article.getId(), likeCount, members);
    }
}
