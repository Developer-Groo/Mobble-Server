package com.mobble.mobbleserver.infrastructure.scheduler.notification;

import com.mobble.mobbleserver.application.notification.outbox.port.provided.PushDispatchPort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPushScheduler {

    private final PushDispatchPort pushDispatchPort;

    @Scheduled(fixedDelay = 3_000)
    public void scheduled() {
        pushDispatchPort.processOnce();
    }
}
