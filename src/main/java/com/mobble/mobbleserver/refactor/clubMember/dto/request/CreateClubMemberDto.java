package com.mobble.mobbleserver.refactor.clubMember.dto.request;

import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.refactor.clubMember.entity.JoinStatus;
import com.mobble.mobbleserver.refactor.member.entity.Member;

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
