package com.mobble.mobbleserver.infrastructure.persistence.notification.outbox;

import com.mobble.mobbleserver.application.notification.outbox.port.required.OutBoxReadPort;
import com.mobble.mobbleserver.application.notification.outbox.port.required.OutBoxWritePort;
import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PushOutboxPersistenceAdapter implements OutBoxWritePort, OutBoxReadPort {

    private final JpaPushOutboxRepository repository;

    /* PushOutboxWritePort */
    @Override
    public PushOutbox save(PushOutbox pushOutbox) {
        return repository.save(pushOutbox);
    }

    @Override
    public void saveAll(List<PushOutbox> batch) {
        repository.saveAll(batch);
    }

    /* PushOutboxReadPort */
    @Override
    public List<PushOutbox> claimPendingBatch(int size, LocalDateTime now) {
        List<PushOutbox> list = repository.findPending(now, PageRequest.of(0, size));
        list.forEach(PushOutbox::markProcessing);
        return list;
    }
}
