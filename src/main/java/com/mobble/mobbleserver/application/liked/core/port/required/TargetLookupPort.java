package com.mobble.mobbleserver.application.liked.core.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.infrastructure.web.like.core.dto.command.TargetInfo;

public interface TargetLookupPort {

    TargetInfo targetLoad(LikeType likeType, Long targetId);
}
