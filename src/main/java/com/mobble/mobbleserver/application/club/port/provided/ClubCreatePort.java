package com.mobble.mobbleserver.application.club.port.provided;

import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.response.ClubResponseDto;

public interface ClubCreatePort {

    ClubResponseDto createClub(Long memberId, ClubRequestDto dto);
}
