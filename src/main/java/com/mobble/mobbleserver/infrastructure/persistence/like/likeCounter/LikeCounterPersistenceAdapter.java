package com.mobble.mobbleserver.infrastructure.persistence.like.likeCounter;

import com.mobble.mobbleserver.application.liked.likeCounter.port.required.LikeCounterReadPort;
import com.mobble.mobbleserver.application.liked.likeCounter.port.required.LikeCounterWritePort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeCounterPersistenceAdapter implements LikeCounterWritePort, LikeCounterReadPort {

    private final JpaLikeCounterRepository jpaLikeCounterRepository;

    /**
     * LikeCounterWritePort
     */
    @Override
    public void increment(LikeType likeType, Long targetId) {
        jpaLikeCounterRepository.upsertIncrement(likeType.name(), targetId);
    }

    @Override
    public void safeDecrement(LikeType likeType, Long targetId) {
        jpaLikeCounterRepository.safeDecrement(likeType.name(), targetId);
    }

    /**
     * LikeCounterReadPort
     */
    @Override
    public List<LikeCounter> findAllByLikeTypeAndTargetIds(LikeType likeType, List<Long> targetIds) {
        return jpaLikeCounterRepository.findAllByLikeTypeAndTargetIdIn(likeType, targetIds);
    }
}
