package com.mobble.mobbleserver.application.liked.likeCounter.port.provided;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface LikeCounterQueryPort {

    long getCount(LikeType likeType, Long targetId);
}
