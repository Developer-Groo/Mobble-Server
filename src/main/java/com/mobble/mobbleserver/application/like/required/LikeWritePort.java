package com.mobble.mobbleserver.application.like.required;

import com.mobble.mobbleserver.domain.like.baseLike.BaseLike;

public interface LikeWritePort<T extends BaseLike> {

    T save(T like);

    void delete(T like);
}
