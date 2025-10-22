package com.mobble.mobbleserver.application.like.required;

import com.mobble.mobbleserver.domain.like.baseLike.BaseLike;

import java.util.List;

public interface LikeMemberListReadPort<T extends BaseLike> {

    List<T> getLikeEntities(Long targetId);
}
