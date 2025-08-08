package com.mobble.mobbleserver.domain.like.service;

import com.mobble.mobbleserver.domain.like.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.entity.LikeType;

public interface LikeStrategy {
    LikeType getType();

    LikeToggleResponseDto toggleLike(Long targetId, Long memberId);
}
