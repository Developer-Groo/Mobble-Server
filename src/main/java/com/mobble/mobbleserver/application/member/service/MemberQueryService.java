package com.mobble.mobbleserver.application.member.service;

import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.provided.MemberQueryPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService implements MemberQueryPort {

    private final MemberReadPort memberReadPort;

    @Override
    public Member getMember(Long memberId) {
        return assertMemberByMemberId(memberId);
    }

    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }
}
