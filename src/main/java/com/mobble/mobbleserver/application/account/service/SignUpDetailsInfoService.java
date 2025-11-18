package com.mobble.mobbleserver.application.account.service;

import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.application.account.provided.SignUpDetailsPort;
import com.mobble.mobbleserver.application.account.required.JwtTokenIssuerPort;
import com.mobble.mobbleserver.application.account.required.SignUpTokenPort;
import com.mobble.mobbleserver.application.member.port.required.MemberWritePort;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpDetailsInfoService implements SignUpDetailsPort {

    private final SignUpTokenPort signUpTokenPort;
    private final JwtTokenIssuerPort jwtTokenIssuerPort;

    private final MemberWritePort memberWritePort;

    @Override
    public SocialUserInfo getSocialUserInfo(String signupToken) {

        return signUpTokenPort.extractSignUpInfo(signupToken);
    }

    @Override
    @Transactional
    public String signUp(String signupToken, SignUpRequestDto dto) {
        SocialUserInfo userInfo = signUpTokenPort.extractSignUpInfo(signupToken);

//        Member member = dto.toEntity(userInfo,ground);
//        memberWritePort.save(member);

//        return jwtTokenIssuerPort.issueJwtToken(member.getId());
        return "";
    }
}
