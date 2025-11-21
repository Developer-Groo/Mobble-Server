package com.mobble.mobbleserver.application.club.port.required;

import com.mobble.mobbleserver.domain.club.Club;

import java.util.Optional;

public interface ClubReadPort {

    Optional<Club> findById(Long id);
}
