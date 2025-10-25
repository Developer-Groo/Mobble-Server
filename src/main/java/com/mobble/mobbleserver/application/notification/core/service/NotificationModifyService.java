package com.mobble.mobbleserver.application.notification.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.application.notification.core.port.provided.NotificationIssuePort;
import com.mobble.mobbleserver.application.notification.core.port.provided.NotificationMarkPort;
import com.mobble.mobbleserver.application.notification.core.port.required.NotificationReadPort;
import com.mobble.mobbleserver.application.notification.core.port.required.NotificationWritePort;
import com.mobble.mobbleserver.application.notification.device.port.required.DeviceTokenReadPort;
import com.mobble.mobbleserver.application.notification.outbox.port.required.OutBoxReadPort;
import com.mobble.mobbleserver.application.notification.outbox.port.required.OutBoxWritePort;
import com.mobble.mobbleserver.application.notification.setting.port.required.NotificationSettingReadPort;
import com.mobble.mobbleserver.application.notification.setting.port.required.NotificationSettingWritePort;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.domain.notification.core.Notification;
import com.mobble.mobbleserver.domain.notification.device.DeviceToken;
import com.mobble.mobbleserver.domain.notification.outbox.PushOutbox;
import com.mobble.mobbleserver.domain.notification.setting.NotificationSetting;
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

    private final NotificationWritePort notificationWritePort;
    private final NotificationSettingWritePort notificationSettingWritePort;
    private final OutBoxWritePort outBoxWritePort;

    private final NotificationReadPort notificationReadPort;
    private final DeviceTokenReadPort deviceTokenReadPort;
    private final NotificationSettingReadPort notificationSettingReadPort;
    private final MemberReadPort memberReadPort;

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher events;

    @Override
    public Long issue(SendNotificationCommand command) {
        Member receiver = memberReadPort.findByIdAndIsDeletedFalse(command.receiverId()).orElseThrow();

        Notification saved = notificationWritePort.save(
                Notification.create(receiver, command.type(), command.title(), command.content(), command.targetType(), command.targetId())
        );

        NotificationSetting setting = notificationSettingReadPort.findSettingByMemberId(receiver.getId())
                .orElseGet(() -> notificationSettingWritePort.save(NotificationSetting.defaultOn(receiver)));

        if (!setting.allows(command.type())) return saved.getId();

        List<DeviceToken> tokens = deviceTokenReadPort.findByMemberIdAndEnabledTrue(receiver.getId());

        if (tokens.isEmpty()) return saved.getId();

        Map<String, String> data = Map.of(
                "type", command.type().name(),
                "targetType", command.targetType().name(),
                "targetId", String.valueOf(command.targetId())
        );

        tokens.forEach(t ->
                outBoxWritePort.save(PushOutbox.pending(t.getToken(), command.title(), command.content(), data, objectMapper))
        );

        events.publishEvent(new PushReadyEvent());

        return saved.getId();
    }

    @Override
    public void markRead(Long memberId, Long notificationId) {
        Notification n = notificationReadPort.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("notification not found"));

        if (!Objects.equals(n.getReceiver().getId(), memberId)) throw new IllegalStateException("forbidden");

        n.markAsRead();
    }

    @Override
    public void markAllRead(Long memberId, ReadAllReq request) {
        Long upToId = request == null ? null : request.upToId();
        notificationWritePort.markAllRead(memberId, upToId);
    }

    public record PushReadyEvent() {
    }
}
