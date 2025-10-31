package com.mobble.mobbleserver.infrastructure.persistence.like.likeCounter;

import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaLikeCounterRepository extends JpaRepository<LikeCounter, Long> {

    @Modifying
    @Query(value = """
    INSERT INTO like_counter (like_type, target_id, cnt)
    VALUES (:likeType, :targetId, 1)
    ON DUPLICATE KEY UPDATE cnt = cnt + 1
    """, nativeQuery = true)
    int upsertIncrement(@Param("likeType") String likeType,
                        @Param("targetId") Long targetId);

    @Modifying
    @Query(value = """
    UPDATE like_counter
    SET cnt = GREATEST(cnt - 1, 0)
    WHERE like_type = :likeType AND target_id = :targetId
    """, nativeQuery = true)
    int safeDecrement(@Param("likeType") String likeType,
                      @Param("targetId") Long targetId);

    List<LikeCounter> findAllByLikeTypeAndTargetIdIn(LikeType likeType, List<Long> targetIds);
}
