package com.mobble.mobbleserver.account.auth.service;

import com.mobble.mobbleserver.account.auth.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.account.auth.dto.response.SignUpDetailsInfoResponseDto;
import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpDetailsInfoService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public SignUpDetailsInfoResponseDto getSocialUserInfo(String signupToken) {
        SocialUserInfo userInfo = tokenProvider.getSignupTokenInfo(signupToken);

        return SignUpDetailsInfoResponseDto.toDto(userInfo);
    }

    @Transactional
    public String signup(String signupToken, SignUpRequestDto dto) {
        SocialUserInfo userInfo = tokenProvider.getSignupTokenInfo(signupToken);
        Member member = dto.toEntity(userInfo);
        memberRepository.save(member);

        return tokenProvider.createJwtToken(member.getId());
    }
}
