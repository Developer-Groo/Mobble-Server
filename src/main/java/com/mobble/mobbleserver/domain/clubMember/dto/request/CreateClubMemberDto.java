package com.mobble.mobbleserver.domain.clubMember.dto.request;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.entity.JoinStatus;
import com.mobble.mobbleserver.domain.member.entity.Member;

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
