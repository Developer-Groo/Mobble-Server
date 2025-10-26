package com.mobble.mobbleserver.application.notification.outbox.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobble.mobbleserver.application.notification.outbox.port.provided.PushDispatchPort;
import com.mobble.mobbleserver.application.notification.outbox.port.required.OutBoxReadPort;
import com.mobble.mobbleserver.application.notification.outbox.port.required.OutBoxWritePort;
import com.mobble.mobbleserver.application.notification.outbox.port.required.PushProviderPort;
import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PushDispatchService implements PushDispatchPort {

    private final OutBoxReadPort outBoxReadPort;
    private final OutBoxWritePort outBoxWritePort;

    private final PushProviderPort pushProviderPort;
    private final ObjectMapper objectMapper;

    private static final int BATCH_SIZE = 100;

    @Override
    public void processOnce() {
        List<PushOutbox> batch = outBoxReadPort.claimPendingBatch(BATCH_SIZE, LocalDateTime.now());
        if (batch.isEmpty()) return;

        for (PushOutbox outbox : batch) {
            try {
                Map<String, String> data = objectMapper.readValue(outbox.getDataJson(), new TypeReference<>(){});
                pushProviderPort.send(outbox.getToken(), outbox.getTitle(), outbox.getBody(), data);
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

        outBoxWritePort.saveAll(batch);
    }
}
