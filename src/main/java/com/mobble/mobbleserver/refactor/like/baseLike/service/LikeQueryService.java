package com.mobble.mobbleserver.refactor.like.baseLike.service;

import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.refactor.like.baseLike.entity.LikeType;

public interface LikeQueryService {

    LikeType getType();

    LikeMemberListResponseDto getLikedMemberList(Long targetId);
}
