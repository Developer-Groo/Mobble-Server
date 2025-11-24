package com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;

public record ClubMemberResponseDto(
        Long memberId,
        String memberName,
        // todo: 프로필 이미지
        String memberRole,
        JoinStatus joinStatus
) {

    public static ClubMemberResponseDto toDto(ClubMember clubMember) {
        return new ClubMemberResponseDto(
                clubMember.getMember().getId(),
                clubMember.getMember().getName(),
                clubMember.getClubMemberRole().getDisplayName(),
                clubMember.getJoinStatus()
        );
    }
}
