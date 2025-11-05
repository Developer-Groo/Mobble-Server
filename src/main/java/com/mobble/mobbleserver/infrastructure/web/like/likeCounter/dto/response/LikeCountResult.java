package com.mobble.mobbleserver.infrastructure.web.like.likeCounter.dto.response;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public record LikeCountResult(LikeType likeType, Long targetId, Long count) {

    public static LikeCountResult toDto(LikeType likeType, Long targetId, Long count) {
        return new LikeCountResult(likeType, targetId, count);
    }
}
