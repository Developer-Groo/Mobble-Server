package com.mobble.mobbleserver.infrastructure.persistence.club.core;

import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.club.core.port.required.ClubWritePort;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.infrastructure.persistence.club.core.projection.ClubLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.request.ClubSearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClubPersistenceAdapter implements ClubWritePort, ClubReadPort {

    private final JpaClubDslRepository repository;

    @Override
    public Club save(Club club) {
        return repository.save(club);
    }

    @Override
    public void delete(Club club) {
        repository.delete(club);
    }

    @Override
    public ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId) {
        return repository.findLikeInfoByClubIdAndMemberId(clubId, memberId);
    }

    @Override
    public Optional<Club> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Club> searchClubs(ClubSearchRequestDto dto) {
        return repository.searchClubs(dto);
    }

    @Override
    public boolean existsById(Long clubId) {
        return repository.existsById(clubId);
    }
}
