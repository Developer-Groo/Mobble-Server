package com.mobble.mobbleserver.refactor.notification.device.service;

import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.member.validator.MemberValidator;
import com.mobble.mobbleserver.refactor.notification.device.entity.DeviceToken;
import com.mobble.mobbleserver.refactor.notification.device.entity.Platform;
import com.mobble.mobbleserver.refactor.notification.device.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.mobble.mobbleserver.refactor.notification.core.dto.NotificationDto.RegisterTokenReq;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    private final MemberReadPort memberReadPort;

    @Transactional
    public DeviceToken register(Long memberId, RegisterTokenReq request) {
        Member member = findMemberByMemberIdOrThrow(memberId);
        Platform platform = Platform.valueOf(request.platform().toUpperCase());

        return deviceTokenRepository.findByToken(request.token())
                .map(deviceToken -> {
                    deviceToken.reassignTo(member);
                    return deviceToken;
                })
                .orElseGet(() ->
                        deviceTokenRepository.save(DeviceToken.create(member, request.token(), platform))
                );
    }

    @Transactional
    public void disable(Long memberId, Long deviceTokenId) {
        DeviceToken deviceToken = deviceTokenRepository.findById(deviceTokenId).orElseThrow();

        if (!Objects.equals(deviceToken.getMember().getId(), memberId)) throw new IllegalStateException("forbidden");

        deviceToken.disable();
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
