package com.mobble.mobbleserver.application.clubMember.port.provided;

import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberResponseDto;

import java.util.List;

public interface ClubMemberQueryPort {

    List<ClubMemberResponseDto> findClubMembers(Long clubId);
}
