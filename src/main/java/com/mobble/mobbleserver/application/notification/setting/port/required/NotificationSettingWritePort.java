package com.mobble.mobbleserver.application.notification.setting.port.required;

import com.mobble.mobbleserver.domain.notification.setting.NotificationSetting;

public interface NotificationSettingWritePort {

    NotificationSetting save(NotificationSetting notificationSetting);
}
