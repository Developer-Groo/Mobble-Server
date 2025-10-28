package com.mobble.mobbleserver.infrastructure.persistence.notification.setting;

import com.mobble.mobbleserver.application.notification.setting.port.required.NotificationSettingReadPort;
import com.mobble.mobbleserver.application.notification.setting.port.required.NotificationSettingWritePort;
import com.mobble.mobbleserver.domain.notification.setting.NotificationSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationSettingPersistenceAdapter implements NotificationSettingWritePort, NotificationSettingReadPort {

    private final JpaNotificationSettingRepository repository;

    /* NotificationSettingWritePort */
    @Override
    public NotificationSetting save(NotificationSetting notificationSetting) {
        return repository.save(notificationSetting);
    }

    /* NotificationSettingReadPort */
    @Override
    public Optional<NotificationSetting> findSettingByMemberId(Long memberId) {
        return repository.findSettingByMember_Id(memberId);
    }
}
