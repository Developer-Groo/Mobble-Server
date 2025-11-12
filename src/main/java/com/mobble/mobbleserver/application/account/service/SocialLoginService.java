package com.mobble.mobbleserver.application.account.service;

import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SocialLoginRequestDto;
import com.mobble.mobbleserver.infrastructure.web.account.dto.response.SocialLoginResponseDto;
import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.account.auth.oauth.verifier.SocialVerifier;
import com.mobble.mobbleserver.account.auth.oauth.verifier.SocialVerifierFactory;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.infrastructure.jwt.TokenProvider;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.clubMember.JpaClubMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialLoginService {

    private final SocialVerifierFactory verifierFactory;
    private final TokenProvider tokenProvider;
    private final JpaClubMemberRepository clubMemberRepository;

    private final MemberReadPort memberReadPort;

    public SocialLoginResponseDto socialLogin(SocialLoginRequestDto dto) {
        SocialVerifier verifier = verifierFactory.getVerifier(dto.socialProvider());
        SocialUserInfo userInfo = verifier.verify(dto.accessToken());

        Member member = findMemberOrThrowIfDeleted(userInfo.socialProvider(), userInfo.socialId());

        if (member != null) {
            // ClubMemberReadPort
            List<ClubMemberRole> roles = clubMemberRepository.findDistinctRolesByMemberIdAndRoleIn(member.getId(), List.of(ClubMemberRole.LEADER, ClubMemberRole.MANAGER));
            String jwtToken = tokenProvider.createJwtToken(member.getId(), roles);

            return SocialLoginResponseDto.existMember(jwtToken);
        }

        if (userInfo.email() == null) throw new DomainException(OAuthErrorCode.NO_USER_INFO); // for Apple Login

        String signupToken = tokenProvider.createSignupToken(userInfo.email(), userInfo.socialProvider(), userInfo.socialId());

        return SocialLoginResponseDto.newMember(signupToken);
    }

    private Member findMemberOrThrowIfDeleted(SocialProvider socialProvider, String socialId) {
        return memberReadPort.findBySocialProviderAndSocialId(socialProvider, socialId)
                .map(member -> {
                    if (member.isDeleted()) {
                        throw new DomainException(MemberErrorCode.FAILED_JOIN);
                    }
                    return member;
                })
                .orElse(null); //신규 회원 이라면 null
    }
}
