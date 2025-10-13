package com.mobble.mobbleserver.refactor.clubMember.dto.response;

import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;

public record ClubMemberRoleUpdateResultDto(ClubMemberUpsertResponseDto clubMember, String jwtToken) {

    public static ClubMemberRoleUpdateResultDto toDto(ClubMember clubMember, String jwtToken) {
        return new ClubMemberRoleUpdateResultDto(
                ClubMemberUpsertResponseDto.toDto(clubMember),
                jwtToken
        );
    }
}
