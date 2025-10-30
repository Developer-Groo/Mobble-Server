package com.mobble.mobbleserver.application.liked.core.port.required;

import java.util.Optional;

public interface LikeReadPort<T> {

    Optional<T> findLike(Long targetId, Long memberId);
}
