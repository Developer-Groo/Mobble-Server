package com.mobble.mobbleserver.infrastructure.persistence.club;

import com.mobble.mobbleserver.application.club.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.club.port.required.ClubWritePort;
import com.mobble.mobbleserver.domain.club.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClubPersistenceAdapter implements ClubWritePort, ClubReadPort {

    private final JpaClubRepository repository;

    /* ClubWritePort */
    @Override
    public Club save(Club club) {
        return repository.save(club);
    }

    @Override
    public void delete(Club club) {
        repository.delete(club);
    }

    /* ClubReadPort */
    @Override
    public Optional<Club> findById(Long id) {
        return repository.findById(id);
    }
}
