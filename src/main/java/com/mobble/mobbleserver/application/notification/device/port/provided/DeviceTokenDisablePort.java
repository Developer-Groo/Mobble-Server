package com.mobble.mobbleserver.application.notification.device.port.provided;

public interface DeviceTokenDisablePort {

    void disable(Long memberId, Long deviceTokenId);
}
