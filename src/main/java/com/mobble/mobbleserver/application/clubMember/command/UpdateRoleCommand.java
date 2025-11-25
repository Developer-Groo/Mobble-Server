package com.mobble.mobbleserver.application.clubMember.command;

import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;

public record UpdateRoleCommand(
        Long clubId,
        Long leaderId,
        Long targetMemberId,
        ClubMemberRole newRole
) {

    public static UpdateRoleCommand create(
            Long clubId,
            Long leaderId,
            Long targetMemberId,
            ClubMemberRole newRole
    ) {
        return new UpdateRoleCommand(clubId, leaderId, targetMemberId, newRole);
    }
}
