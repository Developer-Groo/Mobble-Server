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
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.oAuth.OAuthErrorCode;
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

    public SocialLoginResponseDto socialLogin(SocialLoginRequestDto dto) {
        SocialVerifier verifier = verifierFactory.getVerifier(dto.socialProvider());
        SocialUserInfo userInfo = verifier.verify(dto.accessToken());

        Member member = memberValidator.findMemberOrThrowIfDeleted(userInfo.socialProvider(), userInfo.socialId());

        if (member != null) {
            List<ClubMemberRole> roles = clubMemberRepository.findDistinctRolesByMemberIdAndRoleIn(member.getId(), List.of(ClubMemberRole.LEADER, ClubMemberRole.MANAGER));
            String jwtToken = tokenProvider.createJwtToken(member.getId(), roles);

            return SocialLoginResponseDto.existMember(jwtToken);
        }

        if (userInfo.email() == null) throw new DomainException(OAuthErrorCode.NO_USER_INFO); // for Apple Login

        String signupToken = tokenProvider.createSignupToken(
                userInfo.name(),
                userInfo.email(),
                userInfo.socialProvider(),
                userInfo.socialId());

        return SocialLoginResponseDto.newMember(signupToken);
    }
}
