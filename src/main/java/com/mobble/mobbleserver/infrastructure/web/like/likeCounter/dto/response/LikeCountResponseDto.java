package com.mobble.mobbleserver.infrastructure.web.like.likeCounter.dto.response;

public record LikeCountResponseDto(Long targetId, Long count) {

    public static LikeCountResponseDto toDto(Long targetId, Long count) {
        return new LikeCountResponseDto(targetId, count);
    }
}
