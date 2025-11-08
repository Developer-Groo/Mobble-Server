package com.mobble.mobbleserver.application.like.port.required;

import com.mobble.mobbleserver.domain.like.LikeType;

import java.util.List;

public interface LikeWritePort {

    void save(LikeType likeType, Long targetId, Long memberId);

    void delete(LikeType likeType, Long targetId, Long memberId);

    void deleteByLikeTypeAndTargetId(LikeType likeType, Long targetId);

    void deleteAllByLikeTypeAndTargetIds(LikeType likeType, List<Long> targetIds);
}
