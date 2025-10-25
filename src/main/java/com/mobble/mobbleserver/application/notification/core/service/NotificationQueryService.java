package com.mobble.mobbleserver.application.notification.core.service;

import com.mobble.mobbleserver.application.notification.core.port.provided.NotificationQueryPort;
import com.mobble.mobbleserver.application.notification.core.port.required.NotificationReadPort;
import com.mobble.mobbleserver.domain.notification.core.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService implements NotificationQueryPort {

    private final NotificationReadPort notificationReadPort;

    @Override
    public List<Notification> getList(Long memberId, Long cursorId, int size) {
        return notificationReadPort.findSlice(memberId, cursorId, PageRequest.of(0, size));
    }

    @Override
    public long unreadCount(Long memberId) {
        return notificationReadPort.countByReceiverIdAndIsReadFalse(memberId);
    }
}
