package com.mobble.mobbleserver.application.club.core.port.provided;

import com.mobble.mobbleserver.infrastructure.web.club.core.dto.response.ClubResponseDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.request.ClubSearchRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.response.ClubSummaryDto;

import java.util.List;

public interface ClubQueryPort {

    ClubResponseDto findClubById(Long clubId, Long memberId);

    List<ClubSummaryDto> searchClubs(ClubSearchRequestDto dto, Long memberId);
}
