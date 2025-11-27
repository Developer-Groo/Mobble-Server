package com.mobble.mobbleserver.application.clubMember.result;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.member.Member;

public record ClubMemberSummaryResult(
        Long clubMemberId,
        Long memberId,
        String name,
        String profileImageUrl,
        String role,
        String joinStatus
) {

    public static ClubMemberSummaryResult create(ClubMember clubMember) {
        Member member = clubMember.getMember();

        String imageUrl = member.getProfileImage() != null
                ? member.getProfileImage().getUrl()
                : null;

        return new ClubMemberSummaryResult(
                clubMember.getId(),
                member.getId(),
                member.getName(),
                imageUrl,
                clubMember.getClubMemberRole().name(),
                clubMember.getJoinStatus().name()
        );
    }
}
