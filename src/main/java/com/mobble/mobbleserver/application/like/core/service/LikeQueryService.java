package com.mobble.mobbleserver.application.like.core.service;

import com.mobble.mobbleserver.application.like.core.command.MemberLikedTargetsResult;
import com.mobble.mobbleserver.application.like.core.command.TargetLikedMembersResult;
import com.mobble.mobbleserver.application.like.core.port.provided.LikeQueryPort;
import com.mobble.mobbleserver.application.like.core.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeQueryService implements LikeQueryPort {

    private final LikeReadPort likeReadPort;
    private final MemberReadPort memberReadPort;

    @Override
    public TargetLikedMembersResult findLikedMemberListByTargetId(LikeType likeType, Long targetId) {
        List<Long> likedMemberIds = likeReadPort.findLikedMemberListByTargetId(likeType, targetId);

        if (likedMemberIds.isEmpty()) {
            return TargetLikedMembersResult.toDto(targetId, List.of());
        }

        return TargetLikedMembersResult.toDto(targetId, likedMemberIds);
    }

    @Override
    public MemberLikedTargetsResult findLikedTargetIdListByMemberId(LikeType likeType, Long memberId, List<Long> targetIds) {
        if (targetIds == null || targetIds.isEmpty()) {
            return MemberLikedTargetsResult.toDto(likeType, memberId, List.of());
        }

        Member member = findMemberByMemberIdOrThrow(memberId);

        List<Long> isLikedListByMember = likeReadPort.findLikedTargetIdListByMemberId(likeType, member.getId(), targetIds);

        return MemberLikedTargetsResult.toDto(likeType, member.getId(), isLikedListByMember);
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
