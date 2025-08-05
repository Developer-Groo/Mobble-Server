package com.mobble.mobbleserver.domain.club.club.repository;

import com.mobble.mobbleserver.domain.club.club.repository.dto.ClubLikeInfoDto;

public interface ClubQueryRepository {

    ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId);
}
