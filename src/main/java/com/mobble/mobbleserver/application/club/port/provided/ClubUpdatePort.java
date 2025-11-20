package com.mobble.mobbleserver.application.club.port.provided;

import com.mobble.mobbleserver.application.club.command.UpdateClubCommand;
import com.mobble.mobbleserver.domain.club.Club;

public interface ClubUpdatePort {

    Club update(UpdateClubCommand command);
}
