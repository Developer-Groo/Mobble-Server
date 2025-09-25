package com.mobble.mobbleserver.domain.notification.device.service;

import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.domain.notification.device.entity.DeviceToken;
import com.mobble.mobbleserver.domain.notification.device.entity.Platform;
import com.mobble.mobbleserver.domain.notification.device.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.mobble.mobbleserver.domain.notification.core.dto.NotificationDto.*;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    private final MemberValidator memberValidator;

    @Transactional
    public DeviceToken register(Long memberId, RegisterTokenReq request) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);
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
}
