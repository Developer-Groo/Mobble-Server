package com.mobble.mobbleserver.account.auth.service;

import com.mobble.mobbleserver.account.auth.dto.request.LoginRequestDto;
import com.mobble.mobbleserver.account.auth.dto.response.TokenResponseDto;
import com.mobble.mobbleserver.account.jwt.TokenProvider;
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
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenResponseDto login(LoginRequestDto dto) {
        Member member = memberValidator.findMemberByEmailOrThrow(dto.email());

        // Todo 소셜로그인 구현 전 까지 임시 사용
        if (!member.getPassword().equals(dto.password())) {
            throw new IllegalArgumentException("");
        }

        String accessToken = tokenProvider.createAccessToken(member.getId());

        return TokenResponseDto.toDto(accessToken);
    }
}
