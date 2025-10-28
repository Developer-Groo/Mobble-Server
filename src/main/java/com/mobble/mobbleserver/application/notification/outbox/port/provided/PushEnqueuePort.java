package com.mobble.mobbleserver.application.notification.outbox.port.provided;

import java.util.Map;

public interface PushEnqueuePort {

    Long enqueue(String token, String title, String body, Map<String, String> data);
}
