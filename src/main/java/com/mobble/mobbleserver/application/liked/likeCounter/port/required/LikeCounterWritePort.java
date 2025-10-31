package com.mobble.mobbleserver.application.liked.likeCounter.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface LikeCounterWritePort {

    void increment(LikeType likeType, Long targetId);

    void safeDecrement(LikeType likeType, Long targetId);
}
