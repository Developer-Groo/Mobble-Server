package com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request;

import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import com.mobble.mobbleserver.domain.member.Member;

public record CreateClubMemberDto(
        Member member,
        Club club,
        ClubMemberRole clubMemberRole,
        JoinStatus joinStatus
) {

    public ClubMember toEntity() {
        return ClubMember.createClubMember(
                this.member,
                this.club,
                this.clubMemberRole,
                this.joinStatus
        );
    }

}
