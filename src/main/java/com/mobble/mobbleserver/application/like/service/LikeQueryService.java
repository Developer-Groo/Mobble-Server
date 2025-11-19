package com.mobble.mobbleserver.application.like.service;

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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeQueryService implements LikeQueryPort {

    private final LikeReadPort likeReadPort;
    private final LikeCounterReadPort likeCounterReadPort;
    private final MemberReadPort memberReadPort;

    @Override
    public List<Long> getLikedMemberIds(LikeType likeType, Long targetId) {
        List<Long> likedMemberIds = likeReadPort.findLikedMemberListByTargetId(likeType, targetId);

        if (likedMemberIds.isEmpty()) return Collections.emptyList();

        return likedMemberIds;
    }

    @Override
    public List<Long> getLikedIds(LikeType likeType, Long memberId, List<Long> targetIds) {
        if (targetIds == null || targetIds.isEmpty()) return Collections.emptyList();

        Member member = findMemberByMemberIdOrThrow(memberId);

        return likeReadPort.findLikedTargetIdListByMemberId(likeType, member.getId(), targetIds);
    }

    @Override
    public int getLikeCount(LikeType likeType, Long targetId) {
        return likeCounterReadPort.findByLikeTypeAndTargetId(likeType, targetId)
                .map(LikeCounter::getCount)
                .orElse(0);
    }

    @Override
    public Map<Long, Integer> getLikeCounts(LikeType likeType, List<Long> targetIds) {

        List<LikeCounter> counters = likeCounterReadPort.findAllByLikeTypeAndTargetIds(likeType, targetIds);
        Map<Long, Integer> counterMap = toCounterMap(counters);

        return targetIds.stream()
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        id -> counterMap.getOrDefault(id, 0)
                ));
    }

    /* ==== Private Helper ==== */
    private Map<Long, Integer> toCounterMap(List<LikeCounter> counters) {
        return counters.stream()
                .collect(Collectors.toMap(
                        LikeCounter::getTargetId,
                        LikeCounter::getCount,
                        (a, b) -> b
                ));
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
