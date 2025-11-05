package com.mobble.mobbleserver.infrastructure.persistence.like.core.clubLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.like.core.QClubLike.clubLike;

@RequiredArgsConstructor
public class ClubLikeQueryDslRepositoryImpl implements ClubLikeQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findLikedClubIdListByMemberId(List<Long> clubIds, Long memberId) {
        if (clubIds == null || clubIds.isEmpty()) return List.of();

        return queryFactory
                .select(clubLike.clubId)
                .from(clubLike)
                .where(
                        clubLike.clubId.in(clubIds),
                        clubLike.memberId.eq(memberId)
                )
                .fetch();
    }
}
