package com.mobble.mobbleserver.application.notification.setting.port.provided;

import com.mobble.mobbleserver.domain.notification.setting.NotificationSetting;

public interface NotificationSettingLoadPort {

    NotificationSetting getOrCreate(Long memberId);
}
