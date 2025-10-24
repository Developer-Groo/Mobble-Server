package com.mobble.mobbleserver.application.notification.device.port.provided;

import com.mobble.mobbleserver.domain.notification.device.DeviceToken;
import com.mobble.mobbleserver.infrastructure.web.notification.dto.NotificationDto;

public interface DeviceTokenRegisterPort {

    DeviceToken register(Long memberId, NotificationDto.RegisterTokenReq request);
}
