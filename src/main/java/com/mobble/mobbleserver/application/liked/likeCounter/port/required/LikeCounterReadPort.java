package com.mobble.mobbleserver.application.liked.likeCounter.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;

import java.util.List;

public interface LikeCounterReadPort {

    List<LikeCounter> findAllByLikeTypeAndTargetIds(LikeType likeType, List<Long> targetIds);
}
