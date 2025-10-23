package com.mobble.mobbleserver.application.clubMember.port.provided;

import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberUpsertResponseDto;

public interface ClubMemberCreatePort {

    ClubMemberUpsertResponseDto joinClub(Long memberId, Long clubId);
}
