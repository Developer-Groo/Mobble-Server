package com.mobble.mobbleserver.application.member.port.required;

import com.mobble.mobbleserver.domain.member.Member;

public interface MemberWritePort {

    Member save(Member member);

    void deleteAll(Member member);
}
