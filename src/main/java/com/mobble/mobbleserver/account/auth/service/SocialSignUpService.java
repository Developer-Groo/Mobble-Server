package com.mobble.mobbleserver.account.auth.service;

import com.mobble.mobbleserver.account.auth.dto.request.SignUpRequestDto;
import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.account.oauth2.service.SocialProvider;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SocialSignUpService {

    private final MemberValidator memberValidator;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public String signUp(SignUpRequestDto dto) {
        SocialProvider socialProvider = SocialProvider.fromString(dto.socialProvider());
        memberValidator.validateMemberBySocialProviderAndSocialId(socialProvider, dto.socialId());

        Member member = dto.toEntity();
        memberRepository.save(member);

        return tokenProvider.createAccessToken(member.getId());
    }
}
