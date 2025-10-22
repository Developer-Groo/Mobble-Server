package com.mobble.mobbleserver.application.like.provided;

import com.mobble.mobbleserver.domain.like.baseLike.BaseLike;
import com.mobble.mobbleserver.domain.like.baseLike.LikeType;

import java.util.List;

public interface LikeMemberListPort {

    List<? extends BaseLike> getLikeEntities(LikeType likeType, Long targetId);
}
