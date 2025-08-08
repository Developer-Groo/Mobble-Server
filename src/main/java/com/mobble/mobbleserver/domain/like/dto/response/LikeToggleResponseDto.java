package com.mobble.mobbleserver.domain.like.dto.response;

public record LikeToggleResponseDto(Long targetId, boolean isLiked) {

    public static LikeToggleResponseDto toDto(Long targetId, boolean isLiked) {
        return new LikeToggleResponseDto(targetId, isLiked);
    }
}
