package com.mobble.mobbleserver.application.liked.core.command;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public record TargetInfoResult(LikeType likeType, Long targetId, boolean exists) {

    public static TargetInfoResult toDto(LikeType likeType, Long targetId, boolean exists) {
        return new TargetInfoResult(likeType, targetId, exists);
    }
}
