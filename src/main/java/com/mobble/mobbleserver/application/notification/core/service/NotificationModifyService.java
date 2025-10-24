package com.mobble.mobbleserver.application.notification.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.application.notification.core.port.provided.NotificationIssuePort;
import com.mobble.mobbleserver.application.notification.core.port.provided.NotificationMarkPort;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.domain.notification.core.Notification;
import com.mobble.mobbleserver.infrastructure.persistence.notification.core.JpaNotificationRepository;
import com.mobble.mobbleserver.domain.notification.device.DeviceToken;
import com.mobble.mobbleserver.infrastructure.persistence.notification.device.JpaDeviceTokenRepository;
import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;
import com.mobble.mobbleserver.infrastructure.persistence.notification.outbox.JpaPushOutboxRepository;
import com.mobble.mobbleserver.domain.notification.setting.NotificationSetting;
import com.mobble.mobbleserver.infrastructure.persistence.notification.setting.JpaNotificationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.mobble.mobbleserver.infrastructure.web.notification.dto.NotificationDto.ReadAllReq;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationModifyService implements NotificationIssuePort, NotificationMarkPort {

    private final MemberReadPort memberReadPort;

    private final JpaNotificationRepository jpaNotificationRepository;
    private final JpaNotificationSettingRepository settingRepository;
    private final JpaDeviceTokenRepository jpaDeviceTokenRepository;
    private final JpaPushOutboxRepository outboxRepository;

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher events;

    @Override
    public Long issue(SendNotificationCommand command) {
        Member receiver = memberReadPort.findByIdAndIsDeletedFalse(command.receiverId()).orElseThrow();

        Notification saved = jpaNotificationRepository.save(
                Notification.create(receiver, command.type(), command.title(), command.content(), command.targetType(), command.targetId())
        );

        NotificationSetting setting = settingRepository.findSettingByMember_Id(receiver.getId())
                .orElseGet(() -> settingRepository.save(NotificationSetting.defaultOn(receiver)));

        if (!setting.allows(command.type())) return saved.getId();

        List<DeviceToken> tokens = jpaDeviceTokenRepository.findByMember_IdAndEnabledTrue(receiver.getId());

        if (tokens.isEmpty()) return saved.getId();

        Map<String, String> data = Map.of(
                "type", command.type().name(),
                "targetType", command.targetType().name(),
                "targetId", String.valueOf(command.targetId())
        );

        tokens.forEach(t ->
                outboxRepository.save(PushOutbox.pending(t.getToken(), command.title(), command.content(), data, objectMapper))
        );

        events.publishEvent(new PushReadyEvent());

        return saved.getId();
    }

    @Override
    public void markRead(Long memberId, Long notificationId) {
        Notification n = jpaNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("notification not found"));
        if (!Objects.equals(n.getReceiver().getId(), memberId)) throw new IllegalStateException("forbidden");
        n.markAsRead();
    }

    @Override
    public void markAllRead(Long memberId, ReadAllReq request) {
        Long upToId = request == null ? null : request.upToId();
        jpaNotificationRepository.markAllRead(memberId, upToId);
    }

    public record PushReadyEvent() {
    }
}
