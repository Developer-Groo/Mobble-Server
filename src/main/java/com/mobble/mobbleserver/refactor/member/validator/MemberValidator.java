package com.mobble.mobbleserver.refactor.member.validator;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    public Member findMemberOrThrowIfDeleted(SocialProvider socialProvider, String socialId) {
        return memberRepository.findBySocialProviderAndSocialId(socialProvider, socialId)
                .map(member -> {
                    if (member.isDeleted()) {
                        throw new DomainException(MemberErrorCode.FAILED_JOIN);
                    }
                    return member;
                })
                .orElse(null); //신규 회원 이라면 null
    }
}
