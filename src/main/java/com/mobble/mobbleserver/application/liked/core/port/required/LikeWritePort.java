package com.mobble.mobbleserver.application.liked.core.port.required;

import com.mobble.mobbleserver.domain.like.core.AbstractLike;

public interface LikeWritePort<T extends AbstractLike> {

    T save(T like);

    void delete(T like);

}
