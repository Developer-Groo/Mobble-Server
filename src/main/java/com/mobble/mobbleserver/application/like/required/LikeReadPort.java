package com.mobble.mobbleserver.application.like.required;

import com.mobble.mobbleserver.domain.like.baseLike.LikeType;

import java.util.Optional;

public interface LikeReadPort<T> {

    Optional<T> findLike(LikeType likeType, Long targetId, Long memberId);
}
