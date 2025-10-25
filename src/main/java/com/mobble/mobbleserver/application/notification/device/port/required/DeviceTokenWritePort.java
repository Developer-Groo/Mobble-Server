package com.mobble.mobbleserver.application.notification.device.port.required;

import com.mobble.mobbleserver.domain.notification.device.DeviceToken;

public interface DeviceTokenWritePort {

    DeviceToken save(DeviceToken deviceToken);
}
