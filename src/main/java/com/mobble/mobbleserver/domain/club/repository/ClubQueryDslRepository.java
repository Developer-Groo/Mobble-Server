package com.mobble.mobbleserver.domain.club.repository;

import com.mobble.mobbleserver.domain.club.repository.dto.ClubLikeInfoDto;

public interface ClubQueryDslRepository {

    ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId);
}
