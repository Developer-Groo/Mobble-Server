package com.mobble.mobbleserver.domain.notification.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
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
    private Status status;

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
        this.status = Status.PENDING;
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

    public void markSent() {
        this.status = Status.SENT;
        this.lastError = null;
    }

    public void markForRetry(String error) {
        this.status = Status.PENDING;
        this.attempts++;
        this.lastError = error;

        long minutes = switch (attempts) {
            case 1 -> 1;
            case 2 -> 5;
            case 3 -> 15;
            case 4 -> 30;
            default -> 60;
        };
        this.nextAttemptAt = LocalDateTime.now().plusMinutes(minutes);
    }

    public void markFailed(String error) {
        this.status = Status.FAILED;
        this.lastError = error;
    }
}
