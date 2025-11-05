package com.mobble.mobbleserver.application.like.core.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface LikeWritePort {

    void save(LikeType likeType, Long targetId, Long memberId);

    void delete(LikeType likeType, Long targetId, Long memberId);
}
