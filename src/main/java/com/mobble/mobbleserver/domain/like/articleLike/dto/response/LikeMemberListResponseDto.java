package com.mobble.mobbleserver.domain.like.articleLike.dto.response;

import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;

import java.util.List;

public record LikeMemberListResponseDto(Long articleId, List<LikeMemberResponseDto> likedMembers) {

    public static LikeMemberListResponseDto toDto(Long articleId, List<ArticleLike> articleLikes) {
        List<LikeMemberResponseDto> likedMembers = articleLikes.stream()
                .map(articleLike -> LikeMemberResponseDto.toDto(articleLike.getMember()))
                .toList();

        return new LikeMemberListResponseDto(
                articleId,
                likedMembers
        );
    }
}
