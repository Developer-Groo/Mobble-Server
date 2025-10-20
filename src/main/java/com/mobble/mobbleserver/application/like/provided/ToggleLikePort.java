package com.mobble.mobbleserver.application.like.provided;

import com.mobble.mobbleserver.domain.like.baseLike.LikeType;
import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeToggleResponseDto;

public interface ToggleLikePort {

    LikeToggleResponseDto toggleLike(LikeType likeType, Long targetId, Long memberId);
}
