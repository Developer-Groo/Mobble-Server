package com.mobble.mobbleserver.support.fixture.member;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.domain.member.entity.Gender;
import com.mobble.mobbleserver.domain.member.entity.Member;

import java.util.UUID;

public class MemberTestFixture {

    public static Member createDefaultMember() {
        return Member.createMember(
                "name",
                1,
                Gender.FEMALE,
                UUID.randomUUID().toString(),
                "phone",
                "ground",
                "profileImage",
                true,
                true,
                SocialProvider.NAVER,
                "1212"
        );
    }
}
