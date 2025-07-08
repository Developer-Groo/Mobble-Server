package com.mobble.mobbleserver.domain.member.validator;

import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DomainException(MemberErrorCode.JOINED_EMAIL);
        }
    }
}
