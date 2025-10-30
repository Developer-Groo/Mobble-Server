package com.mobble.mobbleserver.application.liked.core.port.provided;

import com.mobble.mobbleserver.domain.like.core.AbstractLike;
import com.mobble.mobbleserver.domain.like.core.LikeType;

import java.util.List;

public interface LikeMemberListPort {

    List<? extends AbstractLike> getLikeEntities(LikeType likeType, Long targetId);
}
