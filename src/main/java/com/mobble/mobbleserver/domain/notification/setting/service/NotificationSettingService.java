package com.mobble.mobbleserver.domain.notification.setting.service;

import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.domain.notification.core.entity.NotificationType;
import com.mobble.mobbleserver.domain.notification.setting.entity.NotificationSetting;
import com.mobble.mobbleserver.domain.notification.setting.repository.NotificationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mobble.mobbleserver.domain.notification.core.dto.NotificationDto.Toggle;

@Service
@RequiredArgsConstructor
public class NotificationSettingService {

    private final NotificationSettingRepository settingRepository;

    private final MemberValidator memberValidator;

    @Transactional
    public NotificationSetting getSettings(Long memberId) {
        NotificationSetting setting = settingRepository.findSettingByMember_Id(memberId)
                .orElseGet(() -> {
                    Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);
                    return settingRepository.save(NotificationSetting.defaultOn(member));
                });
        setting.syncTypesWithEnum();

        return setting;
    }

    @Transactional
    public void toggleGlobal(Long memberId, Toggle request) {
        NotificationSetting setting = settingRepository.findSettingByMember_Id(memberId)
                .orElseThrow();

        if (request.enabled()) {
            setting.enableGlobal();
        } else {
            setting.disableGlobal();
        }
    }

    @Transactional
    public void setType(Long memberId, Toggle request, NotificationType type) {
        NotificationSetting setting = settingRepository.findSettingByMember_Id(memberId)
                .orElseThrow();

        if (request.enabled()) {
            setting.enable(type);
        } else {
            setting.disable(type);
        }
    }
}
