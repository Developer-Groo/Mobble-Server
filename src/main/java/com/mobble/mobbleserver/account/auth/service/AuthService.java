package com.mobble.mobbleserver.account.auth.service;

import com.mobble.mobbleserver.account.auth.dto.request.SocialLoginRequestDto;
import com.mobble.mobbleserver.account.auth.dto.response.SocialLoginResponseDto;
import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.account.oauth2.verifier.SocialUserInfo;
import com.mobble.mobbleserver.account.oauth2.verifier.TokenVerifier;
import com.mobble.mobbleserver.account.oauth2.verifier.TokenVerifierFactory;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final TokenVerifierFactory verifierFactory;
    private final MemberValidator memberValidator;
    private final TokenProvider tokenProvider;

    public SocialLoginResponseDto socialLoginAndSignUp(SocialLoginRequestDto dto) {
        TokenVerifier verifier = verifierFactory.getVerifier(dto.socialProvider());
        SocialUserInfo userInfo = verifier.verify(dto.accessToken());

        Optional<Member> optionalMember = memberValidator.findIsDeletedFalseMemberByProviderAndSocialId(userInfo.socialProvider(), userInfo.socialId());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String accessToken = tokenProvider.createAccessToken(member.getId());

            return SocialLoginResponseDto.toDto(accessToken);
        }
        //Todo 회원가입 로직
        return null;
    }
}
