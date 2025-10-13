package com.mobble.mobbleserver.refactor.notification.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.member.repository.MemberRepository;
import com.mobble.mobbleserver.refactor.notification.core.entity.Notification;
import com.mobble.mobbleserver.refactor.notification.core.repository.NotificationRepository;
import com.mobble.mobbleserver.refactor.notification.device.entity.DeviceToken;
import com.mobble.mobbleserver.refactor.notification.device.repository.DeviceTokenRepository;
import com.mobble.mobbleserver.refactor.notification.outbox.entity.PushOutbox;
import com.mobble.mobbleserver.refactor.notification.outbox.repository.PushOutboxRepository;
import com.mobble.mobbleserver.refactor.notification.setting.entity.NotificationSetting;
import com.mobble.mobbleserver.refactor.notification.setting.repository.NotificationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.mobble.mobbleserver.refactor.notification.core.dto.NotificationDto.ReadAllReq;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationSettingRepository settingRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final PushOutboxRepository outboxRepository;

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher events;

    @Transactional
    public Long issue(SendNotificationCommand command) {
        Member receiver = memberRepository.getReferenceById(command.receiverId());

        Notification saved = notificationRepository.save(
                Notification.create(receiver, command.type(), command.title(), command.content(), command.targetType(), command.targetId())
        );

        NotificationSetting setting = settingRepository.findSettingByMember_Id(receiver.getId())
                .orElseGet(() -> settingRepository.save(NotificationSetting.defaultOn(receiver)));

        if (!setting.allows(command.type())) return saved.getId();

        List<DeviceToken> tokens = deviceTokenRepository.findByMember_IdAndEnabledTrue(receiver.getId());

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

    public List<Notification> getList(Long memberId, Long cursorId, int size) {
        return notificationRepository.findSlice(memberId, cursorId, PageRequest.of(0, size));
    }

    @Transactional
    public void markRead(Long memberId, Long notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("notification not found"));
        if (!Objects.equals(n.getReceiver().getId(), memberId)) throw new IllegalStateException("forbidden");
        n.markAsRead();
    }

    @Transactional
    public void markAllRead(Long memberId, ReadAllReq request) {
        Long upToId = request == null ? null : request.upToId();
        notificationRepository.markAllRead(memberId, upToId);
    }

    @Transactional(readOnly = true)
    public long unreadCount(Long memberId) {
        return notificationRepository.countByReceiver_IdAndIsReadFalse(memberId);
    }

    public record PushReadyEvent() {
    }
}
