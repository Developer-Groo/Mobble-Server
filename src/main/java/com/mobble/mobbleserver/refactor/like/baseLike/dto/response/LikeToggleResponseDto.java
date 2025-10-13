package com.mobble.mobbleserver.refactor.like.baseLike.dto.response;

public record LikeToggleResponseDto(Long targetId, boolean isLiked) {

    public static LikeToggleResponseDto toDto(Long targetId, boolean isLiked) {
        return new LikeToggleResponseDto(targetId, isLiked);
    }
}
