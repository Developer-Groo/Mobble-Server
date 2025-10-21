package com.mobble.mobbleserver.application.like.provided;

import com.mobble.mobbleserver.domain.like.baseLike.LikeType;

public interface LikeTogglePort {

    void toggleLike(LikeType likeType, Long targetId, Long memberId);
}
