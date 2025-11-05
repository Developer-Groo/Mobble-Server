package com.mobble.mobbleserver.application.liked.core.port.provided;

import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.infrastructure.web.like.core.dto.command.MemberLikedTargetsResult;
import com.mobble.mobbleserver.infrastructure.web.like.core.dto.command.TargetLikedMembersResult;

import java.util.List;

public interface LikeQueryPort {

    // targetId 리스트에 좋아요 한 멤버 목록 조회
    TargetLikedMembersResult findLikedMemberListByTargetId(LikeType likeType, Long targetId);

    // targetId 리스트 중 memberId가 좋아요 한 목록 조회
    MemberLikedTargetsResult findLikedTargetIdListByMemberId(LikeType likeType, Long memberId, List<Long> targetIds);
}
