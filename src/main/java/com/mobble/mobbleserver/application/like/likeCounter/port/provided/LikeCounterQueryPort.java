package com.mobble.mobbleserver.application.like.likeCounter.port.provided;

import com.mobble.mobbleserver.application.like.likeCounter.command.LikeCountMapResult;
import com.mobble.mobbleserver.application.like.likeCounter.command.LikeCountResult;
import com.mobble.mobbleserver.domain.like.core.LikeType;

import java.util.List;

public interface LikeCounterQueryPort {

    LikeCountResult findCountByTargetId(LikeType likeType, Long targetId);

    LikeCountMapResult findCountsByTargetIdList(LikeType likeType, List<Long> targetIds);
}
