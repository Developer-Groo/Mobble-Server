package com.mobble.mobbleserver.infrastructure.persistence.notification.core;

import com.mobble.mobbleserver.application.notification.core.port.required.NotificationReadPort;
import com.mobble.mobbleserver.application.notification.core.port.required.NotificationWritePort;
import com.mobble.mobbleserver.domain.notification.core.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationWritePort, NotificationReadPort {

    private final JpaNotificationRepository repository;

    /* NotificationWritePort */
    @Override
    public Notification save(Notification notification) {
        return repository.save(notification);
    }

    @Override
    public void markAllRead(Long memberId, Long upToId) {
        repository.markAllRead(memberId, upToId);
    }

    /* NotificationReadPort */
    @Override
    public Optional<Notification> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Notification> findSlice(Long memberId, Long cursorId, Pageable pageable) {
        return repository.findSlice(memberId, cursorId, pageable);
    }

    @Override
    public long countByReceiverIdAndIsReadFalse(Long memberId) {
        return repository.countByReceiver_IdAndIsReadFalse(memberId);
    }
}
