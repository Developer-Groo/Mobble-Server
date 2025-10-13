package com.mobble.mobbleserver.refactor.member.service;

import com.mobble.mobbleserver.refactor.ground.entity.Ground;
import com.mobble.mobbleserver.refactor.ground.repository.GroundRepository;
import com.mobble.mobbleserver.infrastructure.web.member.dto.request.MemberUpdateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.member.dto.response.MemberResponseDto;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberValidator memberValidator;
    private final GroundRepository groundRepository;

    public MemberResponseDto getMember(Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        return MemberResponseDto.toDto(member);
    }

    @Transactional
    public MemberResponseDto updateMember(Long memberId, MemberUpdateRequestDto dto) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);
        Ground ground = groundRepository.findGroundByCode(dto.groundCode())
                .orElseThrow();
        Member updateMember = member.updateMember(ground, dto.profileImage());

        return MemberResponseDto.toDto(updateMember);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);
        member.softDelete();
    }
}
