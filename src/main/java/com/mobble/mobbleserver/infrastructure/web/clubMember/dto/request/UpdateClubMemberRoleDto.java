package com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request;

import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateClubMemberRoleDto(
        @NotNull(message = "target member id must not be null")
        @Positive(message = "target member id must be positive")
        Long targetMemberId,

        @NotNull(message = "new role must not be null")
        ClubMemberRole newRole
) {
}
