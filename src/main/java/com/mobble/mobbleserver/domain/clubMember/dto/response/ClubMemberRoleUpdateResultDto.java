package com.mobble.mobbleserver.domain.clubMember.dto.response;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;

public record ClubMemberRoleUpdateResultDto(ClubMemberUpsertResponseDto clubMember, String accessToken) {

    public static ClubMemberRoleUpdateResultDto toDto(ClubMember clubMember, String accessToken) {
        return new ClubMemberRoleUpdateResultDto(
                ClubMemberUpsertResponseDto.toDto(clubMember),
                accessToken
        );
    }
}
