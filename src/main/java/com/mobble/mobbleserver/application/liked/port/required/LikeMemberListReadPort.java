package com.mobble.mobbleserver.application.liked.port.required;

import com.mobble.mobbleserver.domain.like.core.AbstractLike;

import java.util.List;

public interface LikeMemberListReadPort<T extends AbstractLike> {

    List<T> getLikeEntities(Long targetId);
}
