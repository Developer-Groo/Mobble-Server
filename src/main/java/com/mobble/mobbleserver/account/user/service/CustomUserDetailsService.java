package com.mobble.mobbleserver.account.user.service;

import com.mobble.mobbleserver.account.user.CustomUserDetails;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
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
        Member member = memberValidator.findOptionalMemberByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("가입된 사용자를 찾을 수 없습니다.")); //Todo ErrorCode 정의 필요

        if (member.isDeleted()) {
            throw new UsernameNotFoundException("탈퇴한 회원은 7일 후 가입할 수 있습니다."); //Todo ErrorCode 적용 필요
        }
        return new CustomUserDetails(member, false, Collections.emptyMap());
    }
}
