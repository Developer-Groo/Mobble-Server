package com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response;

import com.mobble.mobbleserver.application.clubMember.result.ClubMemberSummaryResult;

public record ClubMemberSummaryDto(
        Long clubMemberId,
        Long memberId,
        String name,
        String profileImageUrl,
        String role,
        String joinStatus
) {

    public static ClubMemberSummaryDto toDto(ClubMemberSummaryResult result) {
        return new ClubMemberSummaryDto(
                result.clubMemberId(),
                result.memberId(),
                result.name(),
                result.profileImageUrl(),
                result.role(),
                result.joinStatus()
        );
    }
}
