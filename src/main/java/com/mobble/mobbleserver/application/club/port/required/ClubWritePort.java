package com.mobble.mobbleserver.application.club.port.required;

import com.mobble.mobbleserver.domain.club.Club;

public interface ClubWritePort {

    Club save(Club club);

    void delete(Club club);
}
