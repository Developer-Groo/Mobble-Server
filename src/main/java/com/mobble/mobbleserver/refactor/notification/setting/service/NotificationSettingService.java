package com.mobble.mobbleserver.refactor.notification.setting.service;

import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.notification.core.entity.NotificationType;
import com.mobble.mobbleserver.refactor.notification.setting.entity.NotificationSetting;
import com.mobble.mobbleserver.refactor.notification.setting.repository.NotificationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mobble.mobbleserver.refactor.notification.core.dto.NotificationDto.Toggle;

@Service
@RequiredArgsConstructor
public class NotificationSettingService {

    private final NotificationSettingRepository settingRepository;

    private final MemberReadPort memberReadPort;

    @Transactional
    public NotificationSetting getSettings(Long memberId) {
        NotificationSetting setting = settingRepository.findSettingByMember_Id(memberId)
                .orElseGet(() -> {
                    Member member = findMemberByMemberIdOrThrow(memberId);
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

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
