package com.mobble.mobbleserver.application.notification.setting.port.required;

import com.mobble.mobbleserver.domain.notification.setting.NotificationSetting;

import java.util.Optional;

public interface NotificationSettingReadPort {

    Optional<NotificationSetting> findSettingByMemberId(Long memberId);
}
