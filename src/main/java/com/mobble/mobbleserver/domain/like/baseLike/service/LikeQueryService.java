package com.mobble.mobbleserver.domain.like.baseLike.service;

import com.mobble.mobbleserver.domain.like.baseLike.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.domain.like.baseLike.entity.LikeType;

public interface LikeQueryService {

    LikeType getType();

    LikeMemberListResponseDto getLikedMemberList(Long targetId);
}
