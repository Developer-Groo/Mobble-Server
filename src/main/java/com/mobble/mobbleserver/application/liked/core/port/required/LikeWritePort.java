package com.mobble.mobbleserver.application.liked.core.port.required;

import com.mobble.mobbleserver.domain.like.core.LikeType;

public interface LikeWritePort {

    void save(LikeType likeType, Long targetId, Long memberId);

    void delete(LikeType likeType, Long targetId, Long memberId);
}
