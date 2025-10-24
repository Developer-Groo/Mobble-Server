package com.mobble.mobbleserver.application.notification.setting.port.provided;

import com.mobble.mobbleserver.domain.notification.core.NotificationType;
import com.mobble.mobbleserver.infrastructure.web.notification.dto.NotificationDto;

public interface NotificationSettingCommandPort {

    void setGlobalEnabled(Long memberId, NotificationDto.Toggle request);

    void setTypeEnabled(Long memberId, NotificationDto.Toggle request, NotificationType type);
}
