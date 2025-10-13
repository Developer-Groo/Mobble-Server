package com.mobble.mobbleserver.refactor.notification.setting.repository;

import com.mobble.mobbleserver.refactor.notification.setting.entity.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {

    Optional<NotificationSetting> findSettingByMember_Id(Long memberId);
}
