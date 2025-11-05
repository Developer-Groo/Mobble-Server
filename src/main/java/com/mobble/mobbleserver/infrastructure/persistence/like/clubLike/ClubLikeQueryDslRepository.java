package com.mobble.mobbleserver.infrastructure.persistence.like.clubLike;

import java.util.List;

public interface ClubLikeQueryDslRepository {

    List<Long> findLikedClubIdListByMemberId(List<Long> clubIds, Long memberId);
}
