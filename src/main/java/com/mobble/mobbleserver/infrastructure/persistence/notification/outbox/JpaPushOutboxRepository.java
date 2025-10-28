package com.mobble.mobbleserver.infrastructure.persistence.notification.outbox;

import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaPushOutboxRepository extends JpaRepository<PushOutbox, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT p FROM PushOutbox p
        WHERE p.pushStatus = 'PENDING'
          AND p.nextAttemptAt <= :now
        ORDER BY p.id
    """)
    List<PushOutbox> findPending(@Param("now") LocalDateTime now, Pageable pageable);
}
