package com.mobble.mobbleserver.application.like.port.required;

import com.mobble.mobbleserver.domain.like.LikeType;

import java.util.List;

public interface LikeCounterWritePort {

    void increment(LikeType likeType, Long targetId);

    void decrement(LikeType likeType, Long targetId);

    void deleteByLikeTypeAndTargetId(LikeType likeType, Long targetId);

    void deleteAllByLikeTypeAndTargetIds(LikeType likeType, List<Long> targetIds);
}
