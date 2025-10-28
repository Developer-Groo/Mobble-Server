package com.mobble.mobbleserver.application.notification.device.port.required;

import com.mobble.mobbleserver.domain.notification.device.DeviceToken;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenReadPort {

    Optional<DeviceToken> findById(Long id);

    Optional<DeviceToken> findByToken(String token);

    List<DeviceToken> findByMemberIdAndEnabledTrue(Long memberId);
}
