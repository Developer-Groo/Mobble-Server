package com.mobble.mobbleserver.infrastructure.persistence.like.counter;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface LikeCounterQueryDslRepository {

    long increment(LikeType likeType, Long targetId);

    long safeDecrement(LikeType likeType, Long targetId);

    void createIfAbsent(LikeType likeType, Long targetId);
}
