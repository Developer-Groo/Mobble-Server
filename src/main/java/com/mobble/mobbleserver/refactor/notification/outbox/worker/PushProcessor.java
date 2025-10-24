package com.mobble.mobbleserver.refactor.notification.outbox.worker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobble.mobbleserver.config.push.PushClient;
import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;
import com.mobble.mobbleserver.domain.notification.outbox.Status;
import com.mobble.mobbleserver.infrastructure.persistence.notification.outbox.PushOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushProcessor {

    private final PushOutboxRepository outboxRepository;
    private final PushClient pushClient;
    private final ObjectMapper objectMapper;

    private static final int BATCH = 100;

    @Transactional
    public void processOnce() {
        List<PushOutbox> batch = outboxRepository.pickPending(Status.PENDING, LocalDateTime.now(), PageRequest.of(0, BATCH));
        if (batch.isEmpty()) return;

        for (PushOutbox outbox : batch) {
            try {
                Map<String, String> data = objectMapper.readValue(outbox.getDataJson(), new TypeReference<>(){});
                pushClient.send(outbox.getToken(), outbox.getTitle(), outbox.getBody(), data);
                outbox.markSent();
            } catch (Exception ex) {
                log.warn("Push send failed id={} err={}", outbox.getId(), ex.getMessage());

                if (outbox.getAttempts() >= 5) {
                    outbox.markFailed(ex.getMessage());
                } else {
                    outbox.markForRetry(ex.getMessage());
                }
            }
        }
    }
}
