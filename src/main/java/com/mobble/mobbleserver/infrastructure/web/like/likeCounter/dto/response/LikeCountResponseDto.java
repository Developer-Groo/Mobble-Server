package com.mobble.mobbleserver.infrastructure.web.like.likeCounter.dto.response;

import com.mobble.mobbleserver.domain.like.core.LikeType;

// Todo 도메인간 전달용 객체
public record LikeCountResponseDto(LikeType likeType, Long targetId, Long count) {

    public static LikeCountResponseDto toDto(LikeType likeType, Long targetId, Long count) {
        return new LikeCountResponseDto(likeType, targetId, count);
    }
}
