package com.mobble.mobbleserver.application.member.port.provided;

import com.mobble.mobbleserver.domain.member.Member;

public interface MemberQueryPort {

    Member getMember(Long memberId);
}
