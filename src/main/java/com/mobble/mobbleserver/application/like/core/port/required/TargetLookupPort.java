package com.mobble.mobbleserver.application.like.core.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface TargetLookupPort {

    boolean targetLoad(LikeType likeType, Long targetId);
}
