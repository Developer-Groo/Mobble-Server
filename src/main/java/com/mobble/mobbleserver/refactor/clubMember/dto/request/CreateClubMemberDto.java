package com.mobble.mobbleserver.refactor.clubMember.dto.request;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.refactor.clubMember.entity.JoinStatus;
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
