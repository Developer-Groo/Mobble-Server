package com.mobble.mobbleserver.application.member.service;

import com.mobble.mobbleserver.application.member.port.provided.MemberDeletePort;
import com.mobble.mobbleserver.application.member.port.provided.MemberUpdatePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.web.member.dto.request.MemberUpdateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.member.dto.response.MemberResponseDto;
import com.mobble.mobbleserver.refactor.ground.entity.Ground;
import com.mobble.mobbleserver.refactor.ground.repository.GroundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberModifyService implements MemberUpdatePort, MemberDeletePort {

    private final GroundRepository groundRepository;

    private final MemberReadPort memberReadPort;

    @Override
    public MemberResponseDto updateMember(Long memberId, MemberUpdateRequestDto dto) {
        Member member = findMemberByMemberIdOrThrow(memberId);
        Ground ground = groundRepository.findGroundByCode(dto.groundCode())
                .orElseThrow();
        Member updateMember = member.updateMember(ground, dto.profileImage());

        return MemberResponseDto.toDto(updateMember);
    }

    @Override
    public void deleteMember(Long memberId) {
        Member member = findMemberByMemberIdOrThrow(memberId);
        member.softDelete();
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
