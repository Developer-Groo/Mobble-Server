package com.mobble.mobbleserver.application.club.core.port.required;

import com.mobble.mobbleserver.domain.club.core.Club;

public interface ClubWritePort {

    Club save(Club club);

    void delete(Club club);

}
