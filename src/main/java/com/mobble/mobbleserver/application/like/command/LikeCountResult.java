package com.mobble.mobbleserver.application.like.command;

import com.mobble.mobbleserver.domain.like.LikeType;

public record LikeCountResult(LikeType likeType, Long targetId, Long count) {

    public static LikeCountResult toDto(LikeType likeType, Long targetId, Long count) {
        return new LikeCountResult(likeType, targetId, count);
    }
}
