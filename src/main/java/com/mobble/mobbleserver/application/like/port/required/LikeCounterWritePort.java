package com.mobble.mobbleserver.application.like.port.required;

import com.mobble.mobbleserver.domain.like.LikeType;

public interface LikeCounterWritePort {

    void increment(LikeType likeType, Long targetId);

    void decrement(LikeType likeType, Long targetId);
}
