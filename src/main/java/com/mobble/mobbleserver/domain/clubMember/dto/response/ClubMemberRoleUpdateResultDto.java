package com.mobble.mobbleserver.domain.clubMember.dto.response;

public record ClubMemberRoleUpdateResultDto(ClubMemberUpsertResponseDto dto, String accessToken) {
}
