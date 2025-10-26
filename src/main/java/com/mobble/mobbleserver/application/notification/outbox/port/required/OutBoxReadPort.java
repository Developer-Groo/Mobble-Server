package com.mobble.mobbleserver.application.notification.outbox.port.required;

import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;

import java.time.LocalDateTime;
import java.util.List;

public interface OutBoxReadPort {

    List<PushOutbox> claimPendingBatch(int size, LocalDateTime now);
}
