package com.mobble.mobbleserver.domain.clubMember.dto.response;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;

public record ClubMemberRoleUpdateResultDto(ClubMemberUpsertResponseDto clubMember, String jwtToken) {

    public static ClubMemberRoleUpdateResultDto toDto(ClubMember clubMember, String jwtToken) {
        return new ClubMemberRoleUpdateResultDto(
                ClubMemberUpsertResponseDto.toDto(clubMember),
                jwtToken
        );
    }
}
