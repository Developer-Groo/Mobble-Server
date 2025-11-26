package com.mobble.mobbleserver.application.clubMember.port.provided;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;

public interface ClubMemberJoinPort {

    ClubMember join(Long memberId, Long clubId);
}
