package com.mobble.mobbleserver.domain.member.validator;

import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public Member findMemberOrThrow(Long memberId) {
        return memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    // Todo 분리
    public void validateEmailDuplication(String email) {
        Optional<Member> checkMember = memberRepository.findByEmail(email);
        if (checkMember.isPresent()) {
            Member member = checkMember.get();
            if (member.isDeleted()) {
                throw new IllegalArgumentException("");
            } else {
                throw new IllegalArgumentException("");
            }
        }
    }
}
