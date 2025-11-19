package com.mobble.mobbleserver.application.club.port.provided;

import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.response.ClubResponseDto;

public interface ClubUpdatePort {

    ClubResponseDto update(Long clubId, Long memberId, ClubRequestDto dto);
}
