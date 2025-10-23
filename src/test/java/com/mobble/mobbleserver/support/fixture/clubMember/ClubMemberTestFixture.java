package com.mobble.mobbleserver.support.fixture.clubMember;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import com.mobble.mobbleserver.domain.member.Member;

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
