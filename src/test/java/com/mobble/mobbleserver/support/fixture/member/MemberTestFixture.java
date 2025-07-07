package com.mobble.mobbleserver.support.fixture.member;

import com.mobble.mobbleserver.domain.member.entity.Gender;
import com.mobble.mobbleserver.domain.member.entity.Member;

public class MemberTestFixture {

    public static Member createDefaultMember() {
        return Member.createMember(
                "name",
                1,
                Gender.FEMALE,
                "email",
                "phone",
                "ground",
                "profileImage",
                true,
                true
        );
    }
}
