package com.mobble.mobbleserver.domain.like.service;

import com.mobble.mobbleserver.domain.like.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.entity.LikeType;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LikeDispatcherService {

    private final Map<LikeType, LikeStrategy> strategyMap;

    public LikeDispatcherService(List<LikeStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        LikeStrategy::getType,
                        Function.identity(),
                        (a, b) -> a,
                        () -> new EnumMap<>(LikeType.class)
                ));
    }

    public LikeToggleResponseDto toggleLike(LikeType likeType, Long targetId, Long memberId) {
        LikeStrategy strategy = strategyMap.get(likeType);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 LikeType: " + likeType);
        }
        return strategy.toggleLike(targetId, memberId);
    }
}
