package com.mobble.mobbleserver.refactor.notification.outbox.repository;

import com.mobble.mobbleserver.refactor.notification.outbox.entity.PushOutbox;
import com.mobble.mobbleserver.refactor.notification.outbox.entity.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PushOutboxRepository extends JpaRepository<PushOutbox, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT p FROM PushOutbox p
        WHERE p.status = :status
          AND (p.nextAttemptAt IS NULL OR p.nextAttemptAt <= :now)
        ORDER BY p.id ASC
    """)
    List<PushOutbox> pickPending(@Param("status") Status status, @Param("now") LocalDateTime now, Pageable pageable);
}
