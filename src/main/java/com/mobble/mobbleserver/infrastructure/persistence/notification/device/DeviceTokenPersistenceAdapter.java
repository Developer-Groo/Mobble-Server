package com.mobble.mobbleserver.infrastructure.persistence.notification.device;

import com.mobble.mobbleserver.application.notification.device.port.required.DeviceTokenReadPort;
import com.mobble.mobbleserver.application.notification.device.port.required.DeviceTokenWritePort;
import com.mobble.mobbleserver.domain.notification.device.DeviceToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeviceTokenPersistenceAdapter implements DeviceTokenWritePort, DeviceTokenReadPort {

    private final JpaDeviceTokenRepository repository;

    /* DeviceTokenWritePort */
    @Override
    public DeviceToken save(DeviceToken deviceToken) {
        return repository.save(deviceToken);
    }

    /* DeviceTokenReadPort */
    @Override
    public Optional<DeviceToken> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<DeviceToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public List<DeviceToken> findByMemberIdAndEnabledTrue(Long memberId) {
        return repository.findByMember_IdAndEnabledTrue(memberId);
    }
}
