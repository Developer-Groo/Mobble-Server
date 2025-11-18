package com.mobble.mobbleserver.application.club.port.required;

import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.infrastructure.persistence.club.projection.ClubLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubSearchRequestDto;

import java.util.List;
import java.util.Optional;

public interface ClubReadPort {

    ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long id, Long id1);

    Optional<Club> findById(Long id);

    List<Club> searchClubs(ClubSearchRequestDto dto);

    boolean existsById(Long clubId);
}
