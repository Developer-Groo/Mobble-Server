package com.mobble.mobbleserver.infrastructure.persistence.notification.device;

import com.mobble.mobbleserver.domain.notification.device.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByToken(String token);

    List<DeviceToken> findByMember_IdAndEnabledTrue(Long memberId);
}
