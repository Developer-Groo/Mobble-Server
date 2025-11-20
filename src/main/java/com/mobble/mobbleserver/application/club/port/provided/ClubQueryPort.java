package com.mobble.mobbleserver.application.club.port.provided;

import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubSearchRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.response.ClubPreviewResponseDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.response.ClubResponseDto;

import java.util.List;

public interface ClubQueryPort {

    ClubResponseDto findClubById(Long clubId, Long memberId);

    List<ClubPreviewResponseDto> searchClubs(ClubSearchRequestDto dto, Long memberId);
}
