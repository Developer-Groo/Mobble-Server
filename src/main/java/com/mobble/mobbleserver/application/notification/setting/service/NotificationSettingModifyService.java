package com.mobble.mobbleserver.application.notification.setting.service;

import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.application.notification.setting.port.provided.NotificationSettingCommandPort;
import com.mobble.mobbleserver.application.notification.setting.port.provided.NotificationSettingLoadPort;
import com.mobble.mobbleserver.application.notification.setting.port.required.NotificationSettingReadPort;
import com.mobble.mobbleserver.application.notification.setting.port.required.NotificationSettingWritePort;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.domain.notification.core.NotificationType;
import com.mobble.mobbleserver.domain.notification.setting.NotificationSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mobble.mobbleserver.infrastructure.web.notification.dto.NotificationDto.Toggle;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationSettingModifyService implements NotificationSettingLoadPort, NotificationSettingCommandPort {

    private final NotificationSettingWritePort notificationSettingWritePort;

    private final NotificationSettingReadPort notificationSettingReadPort;
    private final MemberReadPort memberReadPort;

    @Override
    public NotificationSetting getOrCreate(Long memberId) {
        NotificationSetting setting = notificationSettingReadPort.findSettingByMemberId(memberId)
                .orElseGet(() -> {
                    Member member = findMemberByMemberIdOrThrow(memberId);
                    return notificationSettingWritePort.save(NotificationSetting.defaultOn(member));
                });
        setting.syncTypesWithEnum();

        return setting;
    }

    @Override
    public void setGlobalEnabled(Long memberId, Toggle request) {
        NotificationSetting setting = notificationSettingReadPort.findSettingByMemberId(memberId)
                .orElseThrow();

        if (request.enabled()) {
            setting.enableGlobal();
        } else {
            setting.disableGlobal();
        }
    }

    @Override
    public void setTypeEnabled(Long memberId, Toggle request, NotificationType type) {
        NotificationSetting setting = notificationSettingReadPort.findSettingByMemberId(memberId)
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
