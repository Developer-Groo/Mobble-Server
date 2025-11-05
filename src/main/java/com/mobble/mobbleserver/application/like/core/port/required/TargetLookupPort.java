package com.mobble.mobbleserver.application.like.core.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.application.like.core.command.TargetInfoResult;

public interface TargetLookupPort {

    TargetInfoResult targetLoad(LikeType likeType, Long targetId);
}
