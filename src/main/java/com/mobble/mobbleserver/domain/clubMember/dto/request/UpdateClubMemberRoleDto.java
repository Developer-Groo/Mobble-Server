package com.mobble.mobbleserver.domain.clubMember.dto.request;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.entity.JoinStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateClubMemberRoleDto(
        @NotNull(message = "CLUB_MEMBER:MEMBER_ID_NOT_NULL")
        Long memberId,

        @NotNull(message = "CLUB_MEMBER:ROLE_NOT_NULL")
        ClubMemberRole newRole
) {
}
