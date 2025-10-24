package com.mobble.mobbleserver.infrastructure.web.notification;

import com.mobble.mobbleserver.domain.notification.core.Notification;
import com.mobble.mobbleserver.domain.notification.core.NotificationType;
import com.mobble.mobbleserver.application.notification.core.service.NotificationService;
import com.mobble.mobbleserver.domain.notification.device.DeviceToken;
import com.mobble.mobbleserver.application.notification.device.service.DeviceTokenService;
import com.mobble.mobbleserver.domain.notification.setting.NotificationSetting;
import com.mobble.mobbleserver.application.notification.setting.service.NotificationSettingService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mobble.mobbleserver.infrastructure.web.notification.dto.NotificationDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationAPI {

    private final NotificationService notificationService;
    private final DeviceTokenService deviceTokenService;
    private final NotificationSettingService notificationSettingService;

    @GetMapping
    public Slice getNotificationList(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "20") @Min(1) int size
    ) {
        List<Notification> notifications = notificationService.getList(memberId, cursorId, size + 1);

        return Slice.toDto(notifications, size);
    }

    @PatchMapping("/{id}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void readOne(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable Long id
    ) {
        notificationService.markRead(memberId, id);
    }

    @PostMapping("/read-all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void readAll(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @RequestBody(required = false) ReadAllReq request
    ) {
        notificationService.markAllRead(memberId, request);
    }

    @GetMapping("/unread-count")
    public UnreadCountRes getUnreadCount(
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        long count = notificationService.unreadCount(memberId);

        return UnreadCountRes.toDto(count);
    }

    @PostMapping("/device-tokens")
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceTokenRes registerToken(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @RequestBody RegisterTokenReq request
    ) {
        DeviceToken deviceToken = deviceTokenService.register(memberId, request);

        return DeviceTokenRes.toDto(deviceToken);
    }

    @PatchMapping("/device-tokens/{id}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableToken(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable(name = "id") Long id
    ) {
        deviceTokenService.disable(memberId, id);
    }

    @GetMapping("/settings")
    public SettingsRes getSettings(
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        NotificationSetting setting = notificationSettingService.getSettings(memberId);

        return SettingsRes.toDto(setting);
    }

    @PatchMapping("/settings/global")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleGlobal(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @RequestBody Toggle request
    ) {
        notificationSettingService.toggleGlobal(memberId, request);
    }

    @PatchMapping("/settings/{type}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleType(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable NotificationType type,
            @RequestBody Toggle request
    ) {
        notificationSettingService.setType(memberId, request, type);
    }
}
