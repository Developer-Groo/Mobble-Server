package com.mobble.mobbleserver.application.like.port.provided;

import com.mobble.mobbleserver.domain.like.LikeType;

import java.util.List;

public interface LikeModifyPort {

    void toggleLike(LikeType likeType, Long targetId, Long memberId);

    void delete(LikeType likeType, Long targetId);

    void deleteAll(LikeType likeType, List<Long> targetIds);
}
