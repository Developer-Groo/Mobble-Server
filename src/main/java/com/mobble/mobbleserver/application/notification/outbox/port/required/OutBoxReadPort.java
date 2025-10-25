package com.mobble.mobbleserver.application.notification.outbox.port.required;

import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;
import com.mobble.mobbleserver.domain.notification.outbox.Status;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OutBoxReadPort {

    List<PushOutbox> pickPending(Status status, LocalDateTime now, Pageable pageable);
}
