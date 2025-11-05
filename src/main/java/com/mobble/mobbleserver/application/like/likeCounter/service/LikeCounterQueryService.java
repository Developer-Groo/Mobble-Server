package com.mobble.mobbleserver.application.like.likeCounter.service;

import com.mobble.mobbleserver.application.like.likeCounter.port.provided.LikeCounterQueryPort;
import com.mobble.mobbleserver.application.like.likeCounter.port.required.LikeCounterReadPort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import com.mobble.mobbleserver.application.like.likeCounter.command.LikeCountResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeCounterQueryService implements LikeCounterQueryPort {

    private final LikeCounterReadPort likeCounterReadPort;

    @Override
    public LikeCountResult findCountByTargetId(LikeType likeType, Long targetId) {
        return likeCounterReadPort.findByLikeTypeAndTargetId(likeType, targetId)
                .map(counter -> LikeCountResult.toDto(
                        likeType,
                        counter.getTargetId(),
                        counter.getCount()
                ))
                .orElseGet(() -> LikeCountResult.toDto(likeType, targetId, 0L));
    }

    @Override
    public List<LikeCountResult> findCountsByTargetIdList(LikeType likeType, List<Long> targetIds) {

        List<LikeCounter> counters = likeCounterReadPort.findAllByLikeTypeAndTargetIds(likeType, targetIds);
        Map<Long, Long> counterMap = toCounterMap(counters);

        return targetIds.stream()
                .map(targetId -> LikeCountResult.toDto(
                        likeType,
                        targetId,
                        counterMap.getOrDefault(targetId, 0L)
                ))
                .toList();
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
