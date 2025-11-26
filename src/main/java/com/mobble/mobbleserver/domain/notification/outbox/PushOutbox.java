package com.mobble.mobbleserver.domain.notification.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobble.mobbleserver.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushOutbox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_outbox_id")
    private Long id;

    @Column(name = "token", nullable = false, length = 512)
    private String token;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @Column(name = "body", nullable = false, length = 512)
    private String body;

    @Column(name = "data_json", nullable = false)
    private String dataJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private PushStatus pushStatus;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @Column(name = "next_attempt_at")
    private LocalDateTime nextAttemptAt;

    @Column(name = "last_error", length = 1000)
    private String lastError;

    @Builder(access = AccessLevel.PRIVATE)
    private PushOutbox(String token, String title, String body, String dataJson) {
        this.token = token;
        this.title = title;
        this.body = body;
        this.dataJson = dataJson;
        this.pushStatus = PushStatus.PENDING;
        this.attempts = 0;
        this.nextAttemptAt = LocalDateTime.now();
    }

    public static PushOutbox pending(String token, String title, String body, Map<String, String> data, ObjectMapper om) {
        try {
            return PushOutbox.builder()
                    .token(token)
                    .title(title)
                    .body(body)
                    .dataJson(om.writeValueAsString(data))
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("");
        }
    }

    public void markProcessing() {
        this.pushStatus = PushStatus.PROCESSING;
    }

    public void markSent() {
        this.pushStatus = PushStatus.SENT;
        this.lastError = null;
    }

    public void markForRetry(String error) {
        this.pushStatus = PushStatus.PENDING;
        this.attempts++;
        this.lastError = error;
        this.nextAttemptAt = computeNextBackoff(attempts);
    }

    public void markFailed(String error) {
        this.pushStatus = PushStatus.FAILED;
        this.lastError = error;
    }

    private LocalDateTime computeNextBackoff(int attempts) {
        long seconds = Math.min((long) Math.pow(2, attempts) * 5L, 600L);
        return LocalDateTime.now().plusSeconds(seconds);
    }
}
