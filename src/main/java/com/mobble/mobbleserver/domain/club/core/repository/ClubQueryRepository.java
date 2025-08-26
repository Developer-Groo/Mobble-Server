package com.mobble.mobbleserver.domain.club.core.repository;

import com.mobble.mobbleserver.domain.club.core.repository.dto.ClubLikeInfoDto;

public interface ClubQueryRepository {

    ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId);
}
