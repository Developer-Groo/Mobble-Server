package com.mobble.mobbleserver.application.clubMember.command;

import com.mobble.mobbleserver.domain.clubMember.JoinStatus;

public record UpdateStatusCommand(
        Long clubId,
        Long leaderId,
        Long targetMemberId,
        JoinStatus targetStatus
) {

    public static UpdateStatusCommand create(
            Long clubId,
            Long leaderId,
            Long targetMemberId,
            JoinStatus targetStatus
    ) {
        return new UpdateStatusCommand(clubId, leaderId, targetMemberId, targetStatus);
    }
}
