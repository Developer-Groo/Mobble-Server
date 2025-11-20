package com.mobble.mobbleserver.application.club.port.provided;

import com.mobble.mobbleserver.application.club.command.UpdateClubCommand;
import com.mobble.mobbleserver.application.club.result.ClubResult;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.response.ClubResponseDto;

public interface ClubUpdatePort {

    Club update(UpdateClubCommand command);
}
