package com.mobble.mobbleserver.application.notification.device.service;

import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.application.notification.device.port.provided.DeviceTokenDisablePort;
import com.mobble.mobbleserver.application.notification.device.port.provided.DeviceTokenRegisterPort;
import com.mobble.mobbleserver.application.notification.device.port.required.DeviceTokenReadPort;
import com.mobble.mobbleserver.application.notification.device.port.required.DeviceTokenWritePort;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.domain.notification.device.DeviceToken;
import com.mobble.mobbleserver.domain.notification.device.Platform;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.mobble.mobbleserver.infrastructure.web.notification.dto.NotificationDto.RegisterTokenReq;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceTokenModifyService implements DeviceTokenRegisterPort, DeviceTokenDisablePort {

    private final DeviceTokenWritePort deviceTokenWritePort;

    private final DeviceTokenReadPort deviceTokenReadPort;
    private final MemberReadPort memberReadPort;

    @Override
    public DeviceToken register(Long memberId, RegisterTokenReq request) {
        Member member = findMemberByMemberIdOrThrow(memberId);
        Platform platform = Platform.valueOf(request.platform().toUpperCase());

        return deviceTokenReadPort.findByToken(request.token())
                .map(deviceToken -> {
                    deviceToken.reassignTo(member);
                    return deviceToken;
                })
                .orElseGet(() ->
                        deviceTokenWritePort.save(DeviceToken.create(member, request.token(), platform))
                );
    }

    @Override
    public void disable(Long memberId, Long deviceTokenId) {
        DeviceToken deviceToken = deviceTokenReadPort.findById(deviceTokenId).orElseThrow();

        if (!Objects.equals(deviceToken.getMember().getId(), memberId)) throw new IllegalStateException("forbidden");

        deviceToken.disable();
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
