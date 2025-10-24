package com.mobble.mobbleserver.application.notification.outbox.push;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Map;

@RequiredArgsConstructor
public class FcmClient implements PushClient {

    private final GoogleCredentials credentials;
    private final ObjectMapper om;
    private final String projectId;
    private final boolean dryRun;

    private volatile AccessToken cached;

    @Override
    public void send(String token, String title, String body, Map<String, String> data) throws Exception {
        String accessToken = getAccessToken();

        Map<String, Object> message = Map.of(
                "message", Map.of(
                        "token", token,
                        "notification", Map.of("title", title, "body", body),
                        "data", data,
                        "apns", Map.of("payload", Map.of("aps", Map.of("sound", "default", "content-available", 1))),
                        "android", Map.of("priority", "HIGH")
                ),
                "validateOnly", dryRun
        );

        String json = om.writeValueAsString(message);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send"))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() >= 200 && res.statusCode() < 300) return;

        throw new RuntimeException("FCM error " + res.statusCode() + ": " + res.body());
    }

    private synchronized String getAccessToken() throws Exception {
        if (cached == null || cached.getExpirationTime().toInstant().isBefore(Instant.now().plusSeconds(60))) {
            credentials.refreshIfExpired();
            cached = credentials.getAccessToken();
        }

        return cached.getTokenValue();
    }
}
