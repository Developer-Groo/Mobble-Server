package com.mobble.mobbleserver.application.notification.core.port.required;

import com.mobble.mobbleserver.domain.notification.core.Notification;

public interface NotificationWritePort {

    Notification save(Notification notification);

    void markAllRead(Long memberId, Long upToId);
}
