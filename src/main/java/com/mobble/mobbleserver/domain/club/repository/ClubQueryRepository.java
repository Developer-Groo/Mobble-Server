package com.mobble.mobbleserver.domain.club.repository;

import com.mobble.mobbleserver.domain.club.repository.dto.ClubLikeInfoDto;

public interface ClubQueryRepository {

    ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId);
}
