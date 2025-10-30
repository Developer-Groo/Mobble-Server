package com.mobble.mobbleserver.application.liked.likeCounter.port.provided;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface LikeCounterModifyPort {

    void increment(LikeType likeType, Long targetId);

    void decrement(LikeType likeType, Long targetId);
}
