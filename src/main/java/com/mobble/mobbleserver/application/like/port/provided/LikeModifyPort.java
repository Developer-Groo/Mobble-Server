package com.mobble.mobbleserver.application.like.port.provided;

import com.mobble.mobbleserver.domain.like.LikeType;

public interface LikeModifyPort {

    void toggleLike(LikeType likeType, Long targetId, Long memberId);

    void increment(LikeType likeType, Long targetId);

    void decrement(LikeType likeType, Long targetId);
}
