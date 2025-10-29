package com.mobble.mobbleserver.application.liked.port.required;

import java.util.Optional;

public interface LikeReadPort<T> {

    Optional<T> findLike(Long targetId, Long memberId);
}
