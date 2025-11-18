package com.mobble.mobbleserver.infrastructure.persistence.club;

import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.infrastructure.persistence.club.projection.ClubLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubSearchRequestDto;

import java.util.List;

public interface ClubQueryDslRepository {

    ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId);

    List<Club> searchClubs(ClubSearchRequestDto dto);
}
