package com.mobble.mobbleserver.application.like.port.required;

import com.mobble.mobbleserver.domain.like.LikeType;

public interface LikeWritePort {

    void save(LikeType likeType, Long targetId, Long memberId);

    void delete(LikeType likeType, Long targetId, Long memberId);
}
