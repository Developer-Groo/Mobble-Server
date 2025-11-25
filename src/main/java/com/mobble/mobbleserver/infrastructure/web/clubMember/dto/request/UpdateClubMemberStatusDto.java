package com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request;

import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateClubMemberStatusDto(
        @NotNull(message = "CLUB_MEMBER:MEMBER_ID_NOT_NULL")
        Long targetMemberId,

        @NotNull(message = "CLUB_MEMBER:STATUS_NOT_NULL")
        JoinStatus targetStatus
) {
}
