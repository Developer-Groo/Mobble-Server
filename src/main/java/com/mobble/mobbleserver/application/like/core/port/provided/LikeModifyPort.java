package com.mobble.mobbleserver.application.like.core.port.provided;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface LikeModifyPort {

    void toggleLike(LikeType likeType, Long targetId, Long memberId);
}
