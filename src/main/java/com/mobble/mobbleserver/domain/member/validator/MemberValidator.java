package com.mobble.mobbleserver.domain.member.validator;

import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public Member findMemberOrThrow(Long memberId) {
        return memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    public void exitsEmailOrThrow(String email) {
        if (memberRepository.existsByEmailAndIsDeletedFalse(email)) {
            throw new DomainException(MemberErrorCode.MEMBER_ALREADY_EXISTS);
        }
    }

    public void exitsWithdrewEmailOrThrow(String email) {
        if (memberRepository.existsByEmailAndIsDeletedTrue(email)) {
            throw new DomainException(MemberErrorCode.FAILED_JOIN);
        }
    }
}
