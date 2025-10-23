package com.mobble.mobbleserver.application.clubMember.port.provided;

import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request.UpdateClubMemberRoleDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request.UpdateClubMemberStatusDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberRoleUpdateResultDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberUpsertResponseDto;

public interface ClubMemberUpdatePort {

    ClubMemberUpsertResponseDto updateClubMemberJoinStatus(Long clubId, Long memberId, UpdateClubMemberStatusDto dto);

    ClubMemberRoleUpdateResultDto updateClubMemberRole(Long clubId, Long memberId, UpdateClubMemberRoleDto dto);
}
