package com.mobble.mobbleserver.application.like.core.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;

import java.util.List;
import java.util.Optional;

public interface LikeCounterReadPort {

    Optional<LikeCounter> findByLikeTypeAndTargetId(LikeType likeType, Long targetId);

    List<LikeCounter> findAllByLikeTypeAndTargetIds(LikeType likeType, List<Long> targetIds);
}
