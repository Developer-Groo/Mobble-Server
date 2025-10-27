package com.mobble.mobbleserver.application.notification.core.port.provided;

import com.mobble.mobbleserver.application.notification.core.command.SendNotificationCommand;

public interface NotificationIssuePort {

    Long issue(SendNotificationCommand command);
}
