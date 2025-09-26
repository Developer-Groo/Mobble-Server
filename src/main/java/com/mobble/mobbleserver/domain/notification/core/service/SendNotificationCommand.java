package com.mobble.mobbleserver.domain.notification.core.service;

import com.mobble.mobbleserver.domain.notification.core.entity.NotificationTargetType;
import com.mobble.mobbleserver.domain.notification.core.entity.NotificationType;

public record SendNotificationCommand(
        Long receiverId,
        NotificationType type,
        String title,
        String content,
        NotificationTargetType targetType,
        Long targetId
) {
}
