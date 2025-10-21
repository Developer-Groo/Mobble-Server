package com.mobble.mobbleserver.application.like.provided;

import com.mobble.mobbleserver.domain.like.baseLike.LikeType;

public interface ToggleLikePort {

    void toggleLike(LikeType likeType, Long targetId, Long memberId);
}
