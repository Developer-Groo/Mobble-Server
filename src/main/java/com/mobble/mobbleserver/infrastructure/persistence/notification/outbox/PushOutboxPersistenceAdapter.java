package com.mobble.mobbleserver.infrastructure.persistence.notification.outbox;

import com.mobble.mobbleserver.application.notification.outbox.port.required.OutBoxReadPort;
import com.mobble.mobbleserver.application.notification.outbox.port.required.OutBoxWritePort;
import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;
import com.mobble.mobbleserver.domain.notification.outbox.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    /* PushOutboxReadPort */
    @Override
    public List<PushOutbox> pickPending(Status status, LocalDateTime now, Pageable pageable) {
        return repository.pickPending(status, now, pageable);
    }
}
