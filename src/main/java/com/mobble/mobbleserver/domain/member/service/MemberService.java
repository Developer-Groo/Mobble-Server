package com.mobble.mobbleserver.domain.member.service;

import com.mobble.mobbleserver.domain.member.dto.request.MemberCreateRequestDto;
import com.mobble.mobbleserver.domain.member.dto.request.MemberUpdateRequestDto;
import com.mobble.mobbleserver.domain.member.dto.response.MemberCreateResponseDto;
import com.mobble.mobbleserver.domain.member.dto.response.MemberResponseDto;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberCreateResponseDto createMember(MemberCreateRequestDto dto) {
        memberRepository.existsByEmail(dto.email());
        Member member = dto.toEntity();
        memberRepository.save(member);

        return MemberCreateResponseDto.toDto(member);
    }

    public MemberResponseDto getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(""));

        return MemberResponseDto.toDto(member);
    }

    @Transactional
    public MemberResponseDto updateMember(Long memberId, MemberUpdateRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(""));
        Member updateMember = member.updateMember(dto.ground(), dto.profileImage());

        return MemberResponseDto.toDto(updateMember);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(""));
        memberRepository.delete(member);
    }
}
