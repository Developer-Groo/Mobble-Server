package com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;

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
