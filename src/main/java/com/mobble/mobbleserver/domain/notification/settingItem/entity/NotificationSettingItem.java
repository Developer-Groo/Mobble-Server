package com.mobble.mobbleserver.domain.notification.settingItem.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.domain.notification.core.entity.NotificationType;
import com.mobble.mobbleserver.domain.notification.setting.entity.NotificationSetting;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationSettingItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_setting_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id", nullable = false)
    private NotificationSetting setting;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Builder(access = AccessLevel.PRIVATE)
    private NotificationSettingItem(NotificationSetting setting, NotificationType type, boolean enabled) {
        this.setting = setting;
        this.type = type;
        this.enabled = enabled;
    }

    public static NotificationSettingItem create(NotificationSetting setting, NotificationType type) {
        return NotificationSettingItem.builder()
                .setting(setting)
                .type(type)
                .enabled(true)
                .build();
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}
