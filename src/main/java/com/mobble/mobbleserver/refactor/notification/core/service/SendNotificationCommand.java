package com.mobble.mobbleserver.refactor.notification.core.service;

import com.mobble.mobbleserver.domain.notification.core.NotificationTargetType;
import com.mobble.mobbleserver.domain.notification.core.NotificationType;

public record SendNotificationCommand(
        Long receiverId,
        NotificationType type,
        String title,
        String content,
        NotificationTargetType targetType,
        Long targetId
) {
}
