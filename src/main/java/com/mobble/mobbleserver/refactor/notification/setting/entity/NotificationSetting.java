package com.mobble.mobbleserver.refactor.notification.setting.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.refactor.member.entity.Member;
import com.mobble.mobbleserver.refactor.notification.core.entity.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_setting_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "push_global", nullable = false)
    private boolean pushGlobal;

    @OneToMany(mappedBy = "setting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationSettingItem> items = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private NotificationSetting(Member member, boolean pushGlobal) {
        this.member = member;
        this.pushGlobal = pushGlobal;
    }

    public static NotificationSetting defaultOn(Member member) {
        NotificationSetting setting = NotificationSetting.builder()
                .member(member)
                .pushGlobal(true)
                .build();

        for (NotificationType type: NotificationType.values()) {
            setting.items.add(NotificationSettingItem.create(setting, type));
        }

        return setting;
    }

    public boolean allows(NotificationType type) {
        if (!pushGlobal) return false;

        return findItem(type)
                .map(NotificationSettingItem::isEnabled)
                .orElse(true);
    }

    public void enableGlobal() {
        this.pushGlobal = true;
    }

    public void disableGlobal() {
        this.pushGlobal = false;
    }

    public void enable(NotificationType type) {
        getOrCreateItem(type).enable();
    }

    public void disable(NotificationType type) {
        getOrCreateItem(type).disable();
    }

    public void syncTypesWithEnum() {
        Set<NotificationType> existing = items.stream()
                .map(NotificationSettingItem::getType)
                .collect(Collectors.toSet());

        for (NotificationType type : NotificationType.values()) {
            if (!existing.contains(type)) items.add(NotificationSettingItem.create(this, type));
        }
    }

    private NotificationSettingItem getOrCreateItem(NotificationType type) {
        return findItem(type).orElseGet(() -> {
            NotificationSettingItem item = NotificationSettingItem.create(this, type);
            items.add(item);
            return item;
        });
    }

    private Optional<NotificationSettingItem> findItem(NotificationType type) {
        return items.stream()
                .filter(item -> item.getType().equals(type))
                .findFirst();
    }
}
