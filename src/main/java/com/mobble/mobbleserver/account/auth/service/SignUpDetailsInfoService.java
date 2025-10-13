package com.mobble.mobbleserver.account.auth.service;

import com.mobble.mobbleserver.account.auth.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.account.auth.dto.response.SignUpDetailsInfoResponseDto;
import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;
import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.refactor.ground.entity.Ground;
import com.mobble.mobbleserver.refactor.ground.repository.GroundRepository;
import com.mobble.mobbleserver.refactor.member.entity.Member;
import com.mobble.mobbleserver.refactor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpDetailsInfoService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final GroundRepository groundRepository;

    public SignUpDetailsInfoResponseDto getSocialUserInfo(String signupToken) {
        SocialUserInfo userInfo = tokenProvider.getSignupTokenInfo(signupToken);

        return SignUpDetailsInfoResponseDto.toDto(userInfo);
    }

    @Transactional
    public String signup(String signupToken, SignUpRequestDto dto) {
        SocialUserInfo userInfo = tokenProvider.getSignupTokenInfo(signupToken);
        Ground ground = groundRepository.findGroundByCode(dto.groundCode()).get();
        Member member = dto.toEntity(userInfo,ground);
        memberRepository.save(member);

        return tokenProvider.createJwtToken(member.getId());
    }
}
