package com.mobble.mobbleserver.application.like.port.provided;

import com.mobble.mobbleserver.domain.like.LikeType;

import java.util.List;
import java.util.Map;

public interface LikeQueryPort {

    List<Long> getLikedMemberIds(LikeType likeType, Long targetId);

    List<Long> getLikedIds(LikeType likeType, Long memberId, List<Long> targetIds);

    Long getLikeCount(LikeType likeType, Long targetId);

    Map<Long, Long> getLikeCounts(LikeType likeType, List<Long> targetIds);
}
