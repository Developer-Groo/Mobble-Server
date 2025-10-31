package com.mobble.mobbleserver.infrastructure.persistence.like.likeCounter;

import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaLikeCounterRepository extends JpaRepository<LikeCounter, Long> {

    List<LikeCounter> findAllByLikeTypeAndTargetIdIn(LikeType likeType, List<Long> targetIds);
}
