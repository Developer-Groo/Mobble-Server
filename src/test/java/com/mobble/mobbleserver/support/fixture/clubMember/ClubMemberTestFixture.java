package com.mobble.mobbleserver.support.fixture.clubMember;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.refactor.clubMember.entity.JoinStatus;

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
