package com.mobble.mobbleserver.domain.clubMember.dto.response;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.JoinStatus;

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
