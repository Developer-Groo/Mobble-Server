package com.mobble.mobbleserver.infrastructure.persistence.club.core;

import com.mobble.mobbleserver.infrastructure.persistence.club.core.projection.ClubLikeInfoDto;

public interface ClubQueryRepository {

    ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId);
}
