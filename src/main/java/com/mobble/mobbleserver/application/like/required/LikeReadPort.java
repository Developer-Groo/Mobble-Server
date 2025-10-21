package com.mobble.mobbleserver.application.like.required;

import java.util.Optional;

public interface LikeReadPort<T> {

    Optional<T> findLike(Long targetId, Long memberId);
}
