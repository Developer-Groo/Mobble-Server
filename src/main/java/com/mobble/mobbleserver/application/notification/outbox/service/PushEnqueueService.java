package com.mobble.mobbleserver.application.notification.outbox.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobble.mobbleserver.application.notification.outbox.port.provided.PushEnqueuePort;
import com.mobble.mobbleserver.application.notification.outbox.port.required.OutBoxWritePort;
import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PushEnqueueService implements PushEnqueuePort {

    private final OutBoxWritePort outBoxWritePort;
    private final ObjectMapper mapper;

    @Override
    public Long enqueue(String token, String title, String body, Map<String, String> data) {
        PushOutbox outbox = PushOutbox.pending(token, title, body, data, mapper);
        PushOutbox saved = outBoxWritePort.save(outbox);

        return saved.getId();
    }
}
