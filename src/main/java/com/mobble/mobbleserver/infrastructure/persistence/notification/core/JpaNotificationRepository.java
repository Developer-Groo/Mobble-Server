package com.mobble.mobbleserver.infrastructure.persistence.notification.core;

import com.mobble.mobbleserver.domain.notification.core.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaNotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
       SELECT n FROM Notification n
       WHERE n.receiver.id = :memberId
         AND (:cursorId IS NULL OR n.id < :cursorId)
       ORDER BY n.id DESC
    """)
    List<Notification> findSlice(@Param("memberId") Long memberId, @Param("cursorId") Long cursorId, Pageable pageable);

    @Modifying
    @Query("""
       UPDATE Notification n
       SET n.isRead = true
       WHERE n.receiver.id = :memberId
         AND n.isRead = false
         AND (:upToId IS NULL OR n.id <= :upToId)
    """)
    int markAllRead(@Param("memberId") Long memberId, @Param("upToId") Long upToId);

    long countByReceiver_IdAndIsReadFalse(Long memberId);
}
