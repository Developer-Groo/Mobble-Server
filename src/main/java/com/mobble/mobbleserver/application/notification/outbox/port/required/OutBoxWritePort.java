package com.mobble.mobbleserver.application.notification.outbox.port.required;

import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;

import java.util.List;

public interface OutBoxWritePort {

    PushOutbox save(PushOutbox pushOutbox);

    void saveAll(List<PushOutbox> batch);
}
