package com.mobble.mobbleserver.application.liked.port.provided;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface LikeTogglePort {

    void toggleLike(LikeType likeType, Long targetId, Long memberId);
}
