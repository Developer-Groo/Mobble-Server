package com.mobble.mobbleserver.application.notification.core.port.provided;

import com.mobble.mobbleserver.domain.notification.core.Notification;

import java.util.List;

public interface NotificationQueryPort {

    List<Notification> getList(Long memberId, Long cursorId, int size);

    long unreadCount(Long memberId);
}
