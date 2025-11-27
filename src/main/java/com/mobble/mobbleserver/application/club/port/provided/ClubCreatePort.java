package com.mobble.mobbleserver.application.club.port.provided;

import com.mobble.mobbleserver.application.club.command.CreateClubCommand;
import com.mobble.mobbleserver.domain.club.Club;

public interface ClubCreatePort {

    Club create(CreateClubCommand command);
}
