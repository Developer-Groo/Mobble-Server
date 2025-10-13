package com.mobble.mobbleserver.refactor.clubMember.dto.response;

import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.entity.JoinStatus;

public record ClubMemberUpsertResponseDto(
        Long clubId,
        Long memberId,
        String memberRole,
        JoinStatus joinStatus
) {

    public static ClubMemberUpsertResponseDto toDto(ClubMember clubMember) {
        return new ClubMemberUpsertResponseDto(
                clubMember.getClub().getId(),
                clubMember.getMember().getId(),
                clubMember.getClubMemberRole().getDisplayName(),
                clubMember.getJoinStatus()
        );
    }
}
