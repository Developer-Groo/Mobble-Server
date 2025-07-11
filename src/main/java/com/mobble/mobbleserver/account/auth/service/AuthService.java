package com.mobble.mobbleserver.account.auth.service;

import com.mobble.mobbleserver.account.auth.dto.request.LoginRequestDto;
import com.mobble.mobbleserver.account.auth.dto.response.LoginResponseDto;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberValidator memberValidator;

    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        Member member = memberValidator.findMemberByEmailOrThrow(dto.email());

        if (!member.getPassword().equals(dto.password())) {
            throw new IllegalArgumentException("");
        }

        return LoginResponseDto.toDto(member);
    }
}
