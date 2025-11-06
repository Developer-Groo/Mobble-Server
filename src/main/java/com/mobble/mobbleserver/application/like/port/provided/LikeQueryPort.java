package com.mobble.mobbleserver.application.like.port.provided;

import com.mobble.mobbleserver.application.like.command.LikeCountMapResult;
import com.mobble.mobbleserver.application.like.command.LikeCountResult;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.application.like.command.MemberLikedTargetsResult;
import com.mobble.mobbleserver.application.like.command.TargetLikedMembersResult;

import java.util.List;

public interface LikeQueryPort {

    // targetId 리스트에 좋아요 한 멤버 목록 조회
    TargetLikedMembersResult findMemberIdsByTargetId(LikeType likeType, Long targetId);

    // targetId 리스트 중 memberId가 좋아요 한 목록 조회
    MemberLikedTargetsResult findLikedTargetIdsByMemberId(LikeType likeType, Long memberId, List<Long> targetIds);

    LikeCountResult findLikeCountByTargetId(LikeType likeType, Long targetId);

    LikeCountMapResult findLikeCountsByTargetIds(LikeType likeType, List<Long> targetIds);
}
