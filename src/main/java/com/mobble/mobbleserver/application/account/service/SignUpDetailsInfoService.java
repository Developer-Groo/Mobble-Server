package com.mobble.mobbleserver.application.account.service;

import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.application.account.required.JwtTokenIssuerPort;
import com.mobble.mobbleserver.application.account.required.SignUpTokenPort;
import com.mobble.mobbleserver.application.ground.required.GroundReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberWritePort;
import com.mobble.mobbleserver.domain.ground.Ground;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.infrastructure.web.account.dto.response.SignUpDetailsInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpDetailsInfoService {

    private final SignUpTokenPort signUpTokenPort;
    private final JwtTokenIssuerPort jwtTokenIssuerPort;

    private final MemberWritePort memberWritePort;
    private final GroundReadPort groundReadPort;

    public SignUpDetailsInfoResponseDto getSocialUserInfo(String signupToken) {
        SocialUserInfo userInfo = signUpTokenPort.extractSignUpInfo(signupToken);

        return SignUpDetailsInfoResponseDto.toDto(userInfo);
    }

    @Transactional
    public String signup(String signupToken, SignUpRequestDto dto) {
        SocialUserInfo userInfo = signUpTokenPort.extractSignUpInfo(signupToken);
        Ground ground = groundReadPort.findById(dto.groundCode())
                .orElseThrow();
        Member member = dto.toEntity(userInfo,ground);
        memberWritePort.save(member);

        return jwtTokenIssuerPort.issueJwtToken(member.getId());
    }
}
