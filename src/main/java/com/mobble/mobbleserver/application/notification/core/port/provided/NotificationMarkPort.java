package com.mobble.mobbleserver.application.notification.core.port.provided;

import com.mobble.mobbleserver.infrastructure.web.notification.dto.NotificationDto;

public interface NotificationMarkPort {

    void markRead(Long memberId, Long notificationId);

    void markAllRead(Long memberId, NotificationDto.ReadAllReq request);
}
