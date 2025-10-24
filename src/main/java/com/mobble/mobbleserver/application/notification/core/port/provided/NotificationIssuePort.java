package com.mobble.mobbleserver.application.notification.core.port.provided;

import com.mobble.mobbleserver.application.notification.core.service.SendNotificationCommand;

public interface NotificationIssuePort {

    Long issue(SendNotificationCommand command);
}
