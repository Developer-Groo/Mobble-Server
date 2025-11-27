package com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request;

import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateClubMemberStatusDto(
        @NotNull(message = "target member id must not be null")
        @Positive(message = "target member id must be positive")
        Long targetMemberId,

        @NotNull(message = "target status must not be null")
        JoinStatus targetStatus
) {
}
