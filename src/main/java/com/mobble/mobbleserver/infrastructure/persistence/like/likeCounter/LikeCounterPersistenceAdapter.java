package com.mobble.mobbleserver.infrastructure.persistence.like.likeCounter;

import com.mobble.mobbleserver.application.like.port.required.LikeCounterReadPort;
import com.mobble.mobbleserver.application.like.port.required.LikeCounterWritePort;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    public void decrement(LikeType likeType, Long targetId) {
        jpaLikeCounterRepository.decrement(likeType.name(), targetId);
    }

    /**
     * LikeCounterReadPort
     */
    @Override
    public Optional<LikeCounter> findByLikeTypeAndTargetId(LikeType likeType, Long targetId) {
        return jpaLikeCounterRepository.findByLikeTypeAndTargetId(likeType, targetId);
    }

    @Override
    public List<LikeCounter> findAllByLikeTypeAndTargetIds(LikeType likeType, List<Long> targetIds) {
        return jpaLikeCounterRepository.findAllByLikeTypeAndTargetIdIn(likeType, targetIds);
    }
}
