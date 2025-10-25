package com.mobble.mobbleserver.application.notification.outbox.port.required;

import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;

public interface OutBoxWritePort {

    PushOutbox save(PushOutbox pushOutbox);
}
