package com.mobble.mobbleserver.domain.like.clubLike.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mobble.mobbleserver.domain.like.clubLike.entity.QClubLike.clubLike;

@Repository
@RequiredArgsConstructor
public class ClubLikeRepositoryImpl implements ClubLikeQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteClubLikeAllByClubId(Long clubId) {
        queryFactory
                .delete(clubLike)
                .where(clubLike.club.id.eq(clubId))
                .execute();
    }
}
