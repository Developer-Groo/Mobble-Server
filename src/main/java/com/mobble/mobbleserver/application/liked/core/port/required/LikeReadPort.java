package com.mobble.mobbleserver.application.liked.core.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;

import java.util.List;

public interface LikeReadPort {

    boolean existsTargetLike(LikeType likeType, Long targetId, Long memberId);

    List<Long> findLikedMemberListByTargetId(LikeType likeType, Long targetId);

    List<Long> findLikedTargetIdListByMemberId(LikeType likeType, Long memberId, List<Long> targetIds);
}
