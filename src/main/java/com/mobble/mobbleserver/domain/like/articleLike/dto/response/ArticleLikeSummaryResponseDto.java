package com.mobble.mobbleserver.domain.like.articleLike.dto.response;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;

import java.util.List;

public record ArticleLikeSummaryResponseDto(
        Long article,
        int likeCount,
        List<ArticleLikeMemberResponseDto> likedMembers
) {
    public static ArticleLikeSummaryResponseDto toDto(
            Article article,
            int likeCount,
            List<ArticleLike> articleLikes
    ) {
        List<ArticleLikeMemberResponseDto> likedMembers = articleLikes.stream()
                .map(articleLike -> ArticleLikeMemberResponseDto.toDto(articleLike.getMember()))
                .toList();

        return new ArticleLikeSummaryResponseDto(
                article.getId(),
                likeCount,
                likedMembers
        );
    }
}
