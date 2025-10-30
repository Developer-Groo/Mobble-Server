package com.mobble.mobbleserver.infrastructure.persistence.like.likeCounter;

import com.mobble.mobbleserver.application.liked.likeCounter.port.required.LikeCounterReadPort;
import com.mobble.mobbleserver.application.liked.likeCounter.port.required.LikeCounterWritePort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeCounterPersistenceAdapter implements LikeCounterWritePort, LikeCounterReadPort {

    private final JpaLikeCounterRepository jpaLikeCounterRepository;

    /**
     * LikeCounterWritePort
     */
    @Override
    public long increment(LikeType likeType, Long targetId) {
        return jpaLikeCounterRepository.increment(likeType, targetId);
    }

    @Override
    public long safeDecrement(LikeType likeType, Long targetId) {
        return jpaLikeCounterRepository.safeDecrement(likeType, targetId);
    }

    @Override
    public void createIfAbsent(LikeType likeType, Long targetId) {
        jpaLikeCounterRepository.createIfAbsent(likeType, targetId);
    }

    /**
     * LikeCounterReadPort
     */
    @Override
    public Optional<LikeCounter> findByLikeTypeAndTargetId(LikeType likeType, Long targetId) {
        return jpaLikeCounterRepository.findByLikeTypeAndTargetId(likeType, targetId);
    }
}
