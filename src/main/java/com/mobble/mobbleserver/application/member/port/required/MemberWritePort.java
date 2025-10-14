package com.mobble.mobbleserver.application.member.port.required;

import com.mobble.mobbleserver.domain.member.Member;

import java.util.List;

public interface MemberWritePort {

    Member save(Member member);

    void deleteAll(List<Member> members);
}
