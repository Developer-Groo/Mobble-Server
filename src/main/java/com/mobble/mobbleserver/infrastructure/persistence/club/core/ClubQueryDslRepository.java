package com.mobble.mobbleserver.infrastructure.persistence.club.core;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.infrastructure.persistence.club.core.projection.ClubLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.request.ClubSearchRequestDto;

import java.util.List;

public interface ClubQueryDslRepository {

    ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId);

    List<Club> searchClubs(ClubSearchRequestDto dto);
}
