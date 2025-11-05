package com.mobble.mobbleserver.application.like.core.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface TargetExistencePort {

    boolean existsTarget(LikeType likeType, Long targetId);
}
