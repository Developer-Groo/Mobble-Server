package com.mobble.mobbleserver.account.auth.service;

import com.mobble.mobbleserver.account.auth.dto.request.SocialLoginRequestDto;
import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.account.oauth2.verifier.SocialUserInfo;
import com.mobble.mobbleserver.account.oauth2.verifier.SocialVerifier;
import com.mobble.mobbleserver.account.oauth2.verifier.SocialVerifierFactory;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialLoginService {

    private final SocialVerifierFactory verifierFactory;
    private final MemberValidator memberValidator;
    private final TokenProvider tokenProvider;

    public String socialLogin(SocialLoginRequestDto dto) {
        SocialVerifier verifier = verifierFactory.getVerifier(dto.socialProvider());
        SocialUserInfo userInfo = verifier.verify(dto.accessToken());

        Member member = memberValidator.findIsDeletedFalseMemberByProviderAndSocialIdOrThrow(userInfo.socialProvider(), userInfo.socialId());

        return tokenProvider.createAccessToken(member.getId());
    }
}
