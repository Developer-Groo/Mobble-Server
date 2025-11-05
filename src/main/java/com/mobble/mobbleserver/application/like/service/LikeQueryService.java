package com.mobble.mobbleserver.application.like.service;

import com.mobble.mobbleserver.application.like.command.LikeCountMapResult;
import com.mobble.mobbleserver.application.like.command.LikeCountResult;
import com.mobble.mobbleserver.application.like.command.MemberLikedTargetsResult;
import com.mobble.mobbleserver.application.like.command.TargetLikedMembersResult;
import com.mobble.mobbleserver.application.like.port.provided.LikeQueryPort;
import com.mobble.mobbleserver.application.like.port.required.LikeCounterReadPort;
import com.mobble.mobbleserver.application.like.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeQueryService implements LikeQueryPort {

    private final LikeReadPort likeReadPort;
    private final LikeCounterReadPort likeCounterReadPort;
    private final MemberReadPort memberReadPort;

    @Override
    public TargetLikedMembersResult findMemberIdsByTargetId(LikeType likeType, Long targetId) {
        List<Long> likedMemberIds = likeReadPort.findLikedMemberListByTargetId(likeType, targetId);

        if (likedMemberIds.isEmpty()) {
            return TargetLikedMembersResult.toDto(targetId, List.of());
        }

        return TargetLikedMembersResult.toDto(targetId, likedMemberIds);
    }

    @Override
    public MemberLikedTargetsResult findLikedTargetIdsByMemberId(LikeType likeType, Long memberId, List<Long> targetIds) {
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

    @Override
    public LikeCountResult findLikeCountByTargetId(LikeType likeType, Long targetId) {
        return likeCounterReadPort.findByLikeTypeAndTargetId(likeType, targetId)
                .map(counter -> LikeCountResult.toDto(
                        likeType,
                        counter.getTargetId(),
                        counter.getCount()
                ))
                .orElseGet(() -> LikeCountResult.toDto(likeType, targetId, 0L));
    }

    @Override
    public LikeCountMapResult findLikeCountsByTargetIds(LikeType likeType, List<Long> targetIds) {

        List<LikeCounter> counters = likeCounterReadPort.findAllByLikeTypeAndTargetIds(likeType, targetIds);
        Map<Long, Long> countsByTargetId = toCounterMap(counters);

        return LikeCountMapResult.toDto(likeType, countsByTargetId);
    }

    private Map<Long, Long> toCounterMap(List<LikeCounter> counters) {
        return counters.stream()
                .collect(Collectors.toMap(
                        LikeCounter::getTargetId,
                        LikeCounter::getCount,
                        (a, b) -> b
                ));
    }
}
