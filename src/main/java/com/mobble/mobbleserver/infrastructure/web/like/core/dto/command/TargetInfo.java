package com.mobble.mobbleserver.infrastructure.web.like.core.dto.command;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public record TargetInfo(LikeType likeType, Long targetId, boolean exists) {

    public static TargetInfo toDto(LikeType likeType, Long targetId, boolean exists) {
        return new TargetInfo(likeType, targetId, exists);
    }
}
