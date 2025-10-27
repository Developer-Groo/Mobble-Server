package com.mobble.mobbleserver.infrastructure.persistence.club.clubGround;

import com.mobble.mobbleserver.application.club.clubGround.port.required.ClubGroundReadPort;
import com.mobble.mobbleserver.application.club.clubGround.port.required.ClubGroundWritePort;
import com.mobble.mobbleserver.domain.club.clubGround.ClubGround;
import com.mobble.mobbleserver.domain.ground.Ground;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClubGroundPersistenceAdapter implements ClubGroundWritePort, ClubGroundReadPort {

    private final JpaClubGroundRepository repository;

    @Override
    public List<ClubGround> findByClubId(Long clubId) {
        return repository.findByClubId(clubId);
    }

    @Override
    public List<Ground> findGroundsByClubId(Long clubId) {
        return repository.findGroundsByClubId(clubId);
    }

    @Override
    public void saveAll(List<ClubGround> clubGrounds) {
        repository.saveAll(clubGrounds);
    }

    @Override
    public void deleteAllByClubId(Long clubId) {
        repository.deleteAllByClubId(clubId);
    }
}
