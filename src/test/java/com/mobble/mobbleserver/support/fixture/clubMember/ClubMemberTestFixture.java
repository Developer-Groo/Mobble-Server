package com.mobble.mobbleserver.support.fixture.clubMember;

import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.entity.JoinStatus;
import com.mobble.mobbleserver.domain.member.entity.Member;

public class ClubMemberTestFixture {

    public static ClubMember createDefaultClubMember(Member member, Club club, ClubMemberRole clubMemberRole, JoinStatus joinStatus) {
        return ClubMember.createClubMember(
                member,
                club,
                clubMemberRole,
                joinStatus
        );
    }
}
