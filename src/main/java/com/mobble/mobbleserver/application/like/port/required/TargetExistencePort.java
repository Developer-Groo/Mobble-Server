package com.mobble.mobbleserver.application.like.port.required;

import com.mobble.mobbleserver.domain.like.LikeType;

public interface TargetExistencePort {

    boolean existsTarget(LikeType likeType, Long targetId);
}
