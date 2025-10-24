package com.mobble.mobbleserver.application.notification.outbox.worker;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.mobble.mobbleserver.application.notification.core.service.NotificationModifyService.PushReadyEvent;

@Component
@RequiredArgsConstructor
public class PushWorker {

    private final PushProcessor pushProcessor;

    @Async("pushExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAfterCommit(PushReadyEvent event) {
        pushProcessor.processOnce();
    }

    @Scheduled(fixedDelay = 3_000)
    public void scheduled() {
        pushProcessor.processOnce();
    }
}
