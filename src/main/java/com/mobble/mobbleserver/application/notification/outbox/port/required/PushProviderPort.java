package com.mobble.mobbleserver.application.notification.outbox.port.required;

import java.util.Map;

public interface PushProviderPort {

    void send(String token, String title, String body, Map<String, String> data) throws Exception;
}
