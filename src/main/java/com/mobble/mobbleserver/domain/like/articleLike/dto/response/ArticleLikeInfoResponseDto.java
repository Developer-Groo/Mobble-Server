package com.mobble.mobbleserver.domain.like.articleLike.dto.response;

import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;

import java.util.List;

public record ArticleLikeInfoResponseDto(
        Long articleId,
        boolean isLiked,
        int likeCount,
        List<ArticleLikeMemberResponseDto> likedMembers
) {
    public static ArticleLikeInfoResponseDto toDto(Long articleId, boolean isLiked, List<ArticleLike> articleLikes) {
        List<ArticleLikeMemberResponseDto> likedMembers = articleLikes.stream()
                .map(articleLike -> ArticleLikeMemberResponseDto.toDto(articleLike.getMember()))
                .toList();

        return new ArticleLikeInfoResponseDto(
                articleId,
                isLiked,
                likedMembers.size(),
                likedMembers
        );
    }
}
