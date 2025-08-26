package com.mobble.mobbleserver.domain.notification.core.entity;

import com.mobble.mobbleserver.common.baseEntity.CreatedAtEntity;
import com.mobble.mobbleserver.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "title", length = 60, nullable = false)
    private String title;

    @Column(name = "content", length = 300, nullable = false)
    private String content;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private NotificationTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Builder(access = AccessLevel.PRIVATE)
    private Notification(
            Member receiver,
            NotificationType type,
            String title,
            String content,
            boolean isRead,
            NotificationTargetType targetType,
            Long targetId
    ) {
        this.receiver = receiver;
        this.type = type;
        this.title = title;
        this.content = content;
        this.isRead = isRead;
        this.targetType = targetType;
        this.targetId = targetId;
    }

    public Notification createNotification(
            Member receiver,
            NotificationType type,
            String title,
            String content,
            NotificationTargetType targetType,
            Long targetId
    ) {
        return Notification.builder()
                .receiver(receiver)
                .type(type)
                .title(title)
                .content(content)
                .isRead(false)
                .targetType(targetType)
                .targetId(targetId)
                .build();
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
