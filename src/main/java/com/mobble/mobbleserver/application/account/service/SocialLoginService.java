package com.mobble.mobbleserver.application.account.service;

import com.mobble.mobbleserver.application.account.command.SocialLoginResult;
import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.application.account.provided.SocialLoginPort;
import com.mobble.mobbleserver.application.account.required.JwtTokenIssuerPort;
import com.mobble.mobbleserver.application.account.required.SignUpTokenPort;
import com.mobble.mobbleserver.application.account.required.SocialIdentityClientPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;
import com.mobble.mobbleserver.infrastructure.web.account.dto.request.SocialLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialLoginService implements SocialLoginPort {

    private final SocialIdentityClientPort socialIdentityClientPort;
    private final JwtTokenIssuerPort jwtTokenIssuerPort;
    private final SignUpTokenPort signUpTokenPort;

    private final MemberReadPort memberReadPort;
    private final ClubMemberReadPort clubMemberReadPort;

    @Override
    public SocialLoginResult socialLogin(SocialLoginRequestDto dto) {
        SocialUserInfo userInfo = socialIdentityClientPort.verify(dto.socialProvider(), dto.accessToken());

        Member member = findMemberOrThrowIfDeleted(userInfo.socialProvider(), userInfo.socialId());

        if (member != null) {
            List<ClubMemberRole> roles = clubMemberReadPort.findDistinctRolesByMemberIdAndRoleIn(member.getId(), List.of(ClubMemberRole.LEADER, ClubMemberRole.MANAGER));
            String jwtToken = jwtTokenIssuerPort.issueJwtToken(member.getId(), roles);

            return SocialLoginResult.existMember(jwtToken);
        }

        if (userInfo.email() == null) throw new DomainException(OAuthErrorCode.NO_USER_INFO); // for Apple Login

        String signupToken = signUpTokenPort.issueSignUpToken(userInfo.email(), userInfo.socialProvider(), userInfo.socialId());

        return SocialLoginResult.newMember(signupToken);
    }

    private Member findMemberOrThrowIfDeleted(SocialProvider socialProvider, String socialId) {
        return memberReadPort.findBySocialProviderAndSocialId(socialProvider, socialId)
                .map(member -> {
                    if (member.isDeleted()) {
                        throw new BusinessException(MemberBusinessError.FAILED_JOIN);
                    }
                    return member;
                })
                .orElse(null); //신규 회원 이라면 null
    }
}
