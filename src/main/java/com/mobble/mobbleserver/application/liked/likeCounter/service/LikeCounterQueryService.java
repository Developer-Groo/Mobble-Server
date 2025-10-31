package com.mobble.mobbleserver.application.liked.likeCounter.service;

import com.mobble.mobbleserver.application.liked.likeCounter.port.provided.LikeCounterQueryPort;
import com.mobble.mobbleserver.application.liked.likeCounter.port.required.LikeCounterReadPort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import com.mobble.mobbleserver.infrastructure.web.like.likeCounter.dto.response.LikeCountResponseDto;
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
    public List<LikeCountResponseDto> getCounts(LikeType likeType, List<Long> targetIds) {
        List<LikeCounter> counters = likeCounterReadPort.findAllByLikeTypeAndTargetIds(likeType, targetIds);

        Map<Long, Long> counterMap = toCounterMap(counters);

        return targetIds.stream()
                .map(targetId -> LikeCountResponseDto.toDto(targetId, counterMap.getOrDefault(targetId, 0L)))
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
