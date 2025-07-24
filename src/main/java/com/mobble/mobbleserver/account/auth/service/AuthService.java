package com.mobble.mobbleserver.account.auth.service;

import com.mobble.mobbleserver.account.auth.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    @Transactional
    public Member registerNewMemberr(SignUpRequestDto dto) {
        memberValidator.exitsEmailOrThrow(dto.email());
        memberValidator.existsIsDeletedEmailOrThrow(dto.email());
        Member newMember = dto.toEntity();

        return memberRepository.save(newMember);
    }
}
