package com.mobble.mobbleserver.application.like.likeCounter.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface LikeCounterWritePort {

    void increment(LikeType likeType, Long targetId);

    void decrement(LikeType likeType, Long targetId);
}
