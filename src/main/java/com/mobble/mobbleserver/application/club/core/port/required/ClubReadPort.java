package com.mobble.mobbleserver.application.club.core.port.required;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.infrastructure.persistence.club.core.projection.ClubLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.request.ClubSearchRequestDto;

import java.util.List;
import java.util.Optional;

public interface ClubReadPort {

    ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long id, Long id1);

    Optional<Club> findById(Long id);

    List<Club> searchClubs(ClubSearchRequestDto dto);
}
