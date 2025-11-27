package com.mobble.mobbleserver.application.clubMember.port.provided;

import com.mobble.mobbleserver.application.clubMember.result.ClubMembersResult;

public interface ClubMemberQueryPort {

    ClubMembersResult getClubMembers(Long clubId);
}
