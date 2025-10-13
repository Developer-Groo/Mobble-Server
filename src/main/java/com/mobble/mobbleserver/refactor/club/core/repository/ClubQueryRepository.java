package com.mobble.mobbleserver.refactor.club.core.repository;

import com.mobble.mobbleserver.refactor.club.core.repository.dto.ClubLikeInfoDto;

public interface ClubQueryRepository {

    ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId);
}
