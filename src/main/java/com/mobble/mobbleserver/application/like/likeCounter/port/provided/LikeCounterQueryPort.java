package com.mobble.mobbleserver.application.like.likeCounter.port.provided;

import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.application.like.likeCounter.command.LikeCountResult;

import java.util.List;

public interface LikeCounterQueryPort {

    LikeCountResult findCountByTargetId(LikeType likeType, Long targetId);

    List<LikeCountResult> findCountsByTargetIdList(LikeType likeType, List<Long> targetIds);
}
