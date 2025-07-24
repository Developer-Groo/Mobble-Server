package com.mobble.mobbleserver.account.user.service;

import com.mobble.mobbleserver.account.user.CustomUserDetails;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.account.AccountValidationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberValidator memberValidator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Long memberId = Long.parseLong(username);
            Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

            return CustomUserDetails.existingMember(member, Collections.emptyMap());
        } catch (NumberFormatException e) {
            throw new DomainException(AccountValidationErrorCode.WRONG_MEMBER_ID_TYPE);
        }
    }
}
