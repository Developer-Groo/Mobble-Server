package com.mobble.mobbleserver.refactor.clubMember.dto.response;

import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.entity.JoinStatus;

public record ClubMemberResponseDto(
        Long memberId,
        String memberName,
        // todo: 프로필 이미지
        String memberRole,
        JoinStatus joinStatus
) {

    public static ClubMemberResponseDto toEntity(ClubMember clubMember) {
        return new ClubMemberResponseDto(
                clubMember.getMember().getId(),
                clubMember.getMember().getName(),
                clubMember.getClubMemberRole().getDisplayName(),
                clubMember.getJoinStatus()
        );
    }
}
