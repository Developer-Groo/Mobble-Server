package com.mobble.mobbleserver.refactor.clubMember.dto.request;

import com.mobble.mobbleserver.refactor.clubMember.entity.JoinStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateClubMemberStatusDto(
        @NotNull(message = "CLUB_MEMBER:MEMBER_ID_NOT_NULL")
        Long memberId,

        @NotNull(message = "CLUB_MEMBER:STATUS_NOT_NULL")
        JoinStatus status
) {
}
