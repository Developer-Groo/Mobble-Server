package com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;

public record ClubMemberRoleUpdateResultDto(ClubMemberUpsertResponseDto clubMember, String jwtToken) {

    public static ClubMemberRoleUpdateResultDto toDto(ClubMember clubMember, String jwtToken) {
        return new ClubMemberRoleUpdateResultDto(
                ClubMemberUpsertResponseDto.toDto(clubMember),
                jwtToken
        );
    }
}
