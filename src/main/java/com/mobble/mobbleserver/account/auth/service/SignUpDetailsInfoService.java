package com.mobble.mobbleserver.account.auth.service;

import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.infrastructure.web.account.dto.response.SignUpDetailsInfoResponseDto;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.infrastructure.jwt.TokenProvider;
import com.mobble.mobbleserver.application.ground.required.GroundReadPort;
import com.mobble.mobbleserver.domain.ground.Ground;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.persistence.member.JpaMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpDetailsInfoService {

    private final TokenProvider tokenProvider;
    private final JpaMemberRepository memberRepository;

    private final GroundReadPort groundReadPort;
    public SignUpDetailsInfoResponseDto getSocialUserInfo(String signupToken) {
        SocialUserInfo userInfo = tokenProvider.getSignupTokenInfo(signupToken);

        return SignUpDetailsInfoResponseDto.toDto(userInfo);
    }

    @Transactional
    public String signup(String signupToken, SignUpRequestDto dto) {
        SocialUserInfo userInfo = tokenProvider.getSignupTokenInfo(signupToken);
        Ground ground = groundReadPort.findById(dto.groundCode())
                .orElseThrow();
        Member member = dto.toEntity(userInfo,ground);
        memberRepository.save(member);

        return tokenProvider.createJwtToken(member.getId());
    }
}
