package com.mobble.mobbleserver.infrastructure.web.like.likeCounter.dto.response;

import com.mobble.mobbleserver.domain.like.core.LikeType;

// Todo 도메인간 전달용 객체
public record LikeCountResult(LikeType likeType, Long targetId, Long count) {

    public static LikeCountResult toDto(LikeType likeType, Long targetId, Long count) {
        return new LikeCountResult(likeType, targetId, count);
    }
}
