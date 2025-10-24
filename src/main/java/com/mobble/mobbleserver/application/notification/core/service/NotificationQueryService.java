package com.mobble.mobbleserver.application.notification.core.service;

import com.mobble.mobbleserver.application.notification.core.port.provided.NotificationQueryPort;
import com.mobble.mobbleserver.domain.notification.core.Notification;
import com.mobble.mobbleserver.infrastructure.persistence.notification.core.JpaNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService implements NotificationQueryPort {

    private final JpaNotificationRepository jpaNotificationRepository;

    @Override
    public List<Notification> getList(Long memberId, Long cursorId, int size) {
        return jpaNotificationRepository.findSlice(memberId, cursorId, PageRequest.of(0, size));
    }

    @Override
    public long unreadCount(Long memberId) {
        return jpaNotificationRepository.countByReceiver_IdAndIsReadFalse(memberId);
    }
}
