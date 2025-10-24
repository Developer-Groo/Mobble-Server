package com.mobble.mobbleserver.infrastructure.web.notification.dto;

import com.mobble.mobbleserver.domain.notification.core.Notification;
import com.mobble.mobbleserver.domain.notification.core.NotificationTargetType;
import com.mobble.mobbleserver.domain.notification.core.NotificationType;
import com.mobble.mobbleserver.domain.notification.device.DeviceToken;
import com.mobble.mobbleserver.domain.notification.setting.NotificationSetting;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationDto {
    public record Item(
            Long id,
            NotificationType type,
            String title,
            String content,
            boolean isRead,
            LocalDateTime createdAt,
            NotificationTargetType targetType,
            Long targetId
    ) {

        public static Item toDto(Notification noti) {
            return new Item(
                    noti.getId(),
                    noti.getType(),
                    noti.getTitle(),
                    noti.getContent(),
                    noti.isRead(),
                    noti.getCreatedAt(),
                    noti.getTargetType(),
                    noti.getTargetId()
            );
        }
    }

    public record Slice(List<Item> items, Long nextCursorId, boolean hasNext) {

        public static Slice toDto(List<Notification> notifications, int size) {
            boolean hasNext = notifications.size() > size;
            if (hasNext) notifications = notifications.subList(0, size);
            Long next = notifications.isEmpty() ? null : notifications.get(notifications.size() - 1).getId();

            return new Slice(
                    notifications.stream().map(Item::toDto).toList(),
                    next,
                    hasNext
            );
        }
    }

    public record Toggle(boolean enabled) {
    }

    public record ReadAllReq(Long upToId) {
    }

    public record RegisterTokenReq(String token, String platform) {
    }

    public record UnreadCountRes(long count) {

        public static UnreadCountRes toDto(long count) {
            return new UnreadCountRes(count);
        }
    }

    public record DeviceTokenRes(Long id, String token, String platform, boolean enabled) {

        public static DeviceTokenRes toDto(DeviceToken deviceToken) {
            return new DeviceTokenRes(
                    deviceToken.getId(),
                    deviceToken.getToken(),
                    deviceToken.getPlatform().name(),
                    deviceToken.isEnabled()
            );
        }
    }

    public record SettingsRes(boolean pushGlobal, List<SettingItemRes> items) {

        public static SettingsRes toDto(NotificationSetting setting) {
            return new SettingsRes(
                    setting.isPushGlobal(),
                    setting.getItems().stream()
                            .map(item -> new SettingItemRes(item.getType().name(), item.isEnabled()))
                            .toList()
            );
        }
    }

    public record SettingItemRes(String type, boolean enabled) {}
}
