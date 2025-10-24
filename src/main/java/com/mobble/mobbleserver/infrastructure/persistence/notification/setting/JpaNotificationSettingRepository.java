package com.mobble.mobbleserver.infrastructure.persistence.notification.setting;

import com.mobble.mobbleserver.domain.notification.setting.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaNotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {

    Optional<NotificationSetting> findSettingByMember_Id(Long memberId);
}
