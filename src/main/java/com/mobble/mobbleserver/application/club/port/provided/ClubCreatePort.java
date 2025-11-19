package com.mobble.mobbleserver.application.club.port.provided;

import com.mobble.mobbleserver.application.club.command.CreateClubCommand;
import com.mobble.mobbleserver.application.club.result.ClubResult;

public interface ClubCreatePort {

    ClubResult create(CreateClubCommand command);
}
