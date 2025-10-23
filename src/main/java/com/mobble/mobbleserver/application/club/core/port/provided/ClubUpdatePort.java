package com.mobble.mobbleserver.application.club.core.port.provided;

import com.mobble.mobbleserver.infrastructure.web.club.core.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.response.ClubResponseDto;

public interface ClubUpdatePort {

    ClubResponseDto updateClub(Long clubId, Long memberId, ClubRequestDto dto);
}
