package com.mobble.mobbleserver.account.auth.service;

import com.mobble.mobbleserver.account.auth.dto.request.SocialLoginRequestDto;
import com.mobble.mobbleserver.account.auth.dto.response.SocialLoginResponseDto;
import com.mobble.mobbleserver.account.auth.oauth.verifier.SocialVerifier;
import com.mobble.mobbleserver.account.auth.oauth.verifier.SocialVerifierFactory;
import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;
import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.repository.ClubMemberRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialLoginService {

    private final SocialVerifierFactory verifierFactory;
    private final MemberValidator memberValidator;
    private final TokenProvider tokenProvider;
    private final ClubMemberRepository clubMemberRepository;
    private final MemberRepository memberRepository;

    public SocialLoginResponseDto socialLogin(SocialLoginRequestDto dto) {
        SocialVerifier verifier = verifierFactory.getVerifier(dto.socialProvider());
        SocialUserInfo userInfo = verifier.verify(dto.accessToken());

        Member member = memberValidator.validateMemberOrThrow(userInfo.socialProvider(), userInfo.socialId());

        if (member != null) {
            List<ClubMemberRole> roles = clubMemberRepository.findDistinctRolesByMemberIdAndRoleIn(member.getId(), List.of(ClubMemberRole.LEADER, ClubMemberRole.MANAGER));
            String accessToken = tokenProvider.createAccessJwtToken(member.getId(), roles);

            return SocialLoginResponseDto.existMember(accessToken);
        }

        String signupToken = tokenProvider.createSignupToken(userInfo.name(), userInfo.email(), userInfo.socialProvider(), userInfo.socialId());

        return SocialLoginResponseDto.newMember(signupToken);
    }

    public void socialLogout(Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        member.increaseTokenVersion();
        memberRepository.save(member);
    }
}
