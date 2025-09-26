package com.mobble.mobbleserver.config.push;

import java.util.Map;

public interface PushClient {
    void send(String token, String title, String body, Map<String, String> data) throws Exception;

    class Noop implements PushClient {
        @Override public void send(String token, String title, String body, Map<String, String> data) {
            // 로컬 개발용
        }
    }
}
