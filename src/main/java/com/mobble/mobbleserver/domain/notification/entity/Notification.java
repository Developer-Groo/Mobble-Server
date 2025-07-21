package com.mobble.mobbleserver.domain.notification.entity;

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
    @Column(name = "type")
    private NotificationType type;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "related")
    private Long related;

    @Builder(access = AccessLevel.PRIVATE)
    private Notification(
            Member receiver,
            NotificationType type,
            String content,
            boolean isRead,
            Long related
    ) {
        this.receiver = receiver;
        this.type = type;
        this.content = content;
        this.isRead = isRead;
        this.related = related;
    }

    public Notification createNotification(
            Member receiver,
            NotificationType type,
            String content,
            Long related
    ) {
        return Notification.builder()
                .receiver(receiver)
                .type(type)
                .content(content)
                .isRead(false)
                .related(related)
                .build();
    }

    public void marksAsRead() {
        this.isRead = true;
    }
}
