package com.mobble.mobbleserver.application.liked.likeCounter.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface LikeCounterWritePort {

    long increment(LikeType likeType, Long targetId);

    long safeDecrement(LikeType likeType, Long targetId);

    void createIfAbsent(LikeType likeType, Long targetId);
}
