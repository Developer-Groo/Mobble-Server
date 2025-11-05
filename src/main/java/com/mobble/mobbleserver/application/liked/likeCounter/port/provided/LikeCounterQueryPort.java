package com.mobble.mobbleserver.application.liked.likeCounter.port.provided;

import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.infrastructure.web.like.likeCounter.dto.response.LikeCountResponseDto;

import java.util.List;

public interface LikeCounterQueryPort {

    LikeCountResponseDto findCountByTargetId(LikeType likeType, Long targetId);

    List<LikeCountResponseDto> findCountsByTargetIdList(LikeType likeType, List<Long> targetIds);
}
