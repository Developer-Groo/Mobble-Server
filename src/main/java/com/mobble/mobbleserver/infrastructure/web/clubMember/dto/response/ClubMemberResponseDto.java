package com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.util.DateTimeUtils;

import java.time.LocalDateTime;

public record ClubMemberResponseDto(
        Long clubMemberId,
        Long memberId,
        String name,
        String profileImageUrl,
        String role,
        String status,
        LocalDateTime statusUpdatedAt
) {

    public static ClubMemberResponseDto toDto(ClubMember clubMember) {
        Member member = clubMember.getMember();

        String imageUrl = member.getProfileImage() != null
                ? member.getProfileImage().getUrl()
                : null;

        return new ClubMemberResponseDto(
                clubMember.getId(),
                member.getId(),
                member.getName(),
                imageUrl,
                clubMember.getClubMemberRole().name(),
                clubMember.getJoinStatus().name(),
                DateTimeUtils.toKST(clubMember.getStatusUpdatedAt())
        );
    }
}
