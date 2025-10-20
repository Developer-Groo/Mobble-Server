package com.mobble.mobbleserver.application.like.provided;

import com.mobble.mobbleserver.domain.like.baseLike.LikeType;
import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeMemberListResponseDto;

public interface LikeMemberListPort {

    LikeMemberListResponseDto getMemberList(LikeType likeType, Long targetId);
}
