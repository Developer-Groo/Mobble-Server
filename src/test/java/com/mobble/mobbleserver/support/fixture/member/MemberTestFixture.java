package com.mobble.mobbleserver.support.fixture.member;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.domain.member.Gender;
import com.mobble.mobbleserver.domain.member.Member;

import java.util.UUID;

public class MemberTestFixture {

    public static Member createDefaultMember() {
        return Member.createMember(
                "name",
                1,
                Gender.FEMALE,
                UUID.randomUUID().toString(),
                "phone",
                null,
                null,
                true,
                true,
                SocialProvider.NAVER,
                "1212"
        );
    }
}
