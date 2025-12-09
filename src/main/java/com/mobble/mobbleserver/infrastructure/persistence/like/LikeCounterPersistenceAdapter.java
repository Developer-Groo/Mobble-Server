package com.mobble.mobbleserver.infrastructure.persistence.like;

import com.mobble.mobbleserver.application.like.port.required.LikeCounterReadPort;
import com.mobble.mobbleserver.application.like.port.required.LikeCounterWritePort;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import com.mobble.mobbleserver.infrastructure.persistence.like.likeCounter.JpaLikeCounterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikeCounterPersistenceAdapter implements LikeCounterWritePort, LikeCounterReadPort {

    private final JpaLikeCounterRepository repository;

    /* LikeCounterWritePort */
    @Override
    public void increment(LikeType likeType, Long targetId) {
        repository.upsertIncrement(likeType.name(), targetId);
    }

    @Override
    public void decrement(LikeType likeType, Long targetId) {
        repository.decrement(likeType.name(), targetId);
    }

    public void deleteByLikeTypeAndTargetId(LikeType likeType, Long targetId) {
        repository.deleteByLikeTypeAndTargetId(likeType, targetId);
    }

    @Override
    public void deleteAllByLikeTypeAndTargetIds(LikeType likeType, List<Long> targetIds) {
        repository.deleteAllByLikeTypeAndTargetIdIn(likeType, targetIds);
    }

    /* LikeCounterReadPort */
    @Override
    public Optional<LikeCounter> findByLikeTypeAndTargetId(LikeType likeType, Long targetId) {
        return repository.findByLikeTypeAndTargetId(likeType, targetId);
    }

    @Override
    public List<LikeCounter> findAllByLikeTypeAndTargetIds(LikeType likeType, List<Long> targetIds) {
        return repository.findAllByLikeTypeAndTargetIdIn(likeType, targetIds);
    }
}
