package com.mobble.mobbleserver.domain.club.clubAgeGroup.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mobble.mobbleserver.domain.club.clubAgeGroup.entity.QClubAgeGroup.clubAgeGroup;

@Repository
@RequiredArgsConstructor
public class ClubAgeGroupRepositoryImpl implements ClubAgeGroupQueryDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllByClubId(Long clubId) {
        queryFactory.delete(clubAgeGroup)
                .where(clubAgeGroup.club.id.eq(clubId))
                .execute();
    }
}
