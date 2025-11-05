package com.mobble.mobbleserver.application.like.likeCounter.service;

import com.mobble.mobbleserver.application.like.core.command.LikeCountMapResult;
import com.mobble.mobbleserver.application.like.core.command.LikeCountResult;
import com.mobble.mobbleserver.application.like.core.port.provided.LikeCounterQueryPort;
import com.mobble.mobbleserver.application.like.likeCounter.port.required.LikeCounterReadPort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
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
