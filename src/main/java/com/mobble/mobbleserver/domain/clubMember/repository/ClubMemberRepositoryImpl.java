package com.mobble.mobbleserver.domain.clubMember.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mobble.mobbleserver.domain.clubMember.entity.QClubMember.clubMember;

@Repository
@RequiredArgsConstructor
public class ClubMemberRepositoryImpl implements ClubMemberQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllByClubId(Long clubId) {
        queryFactory.delete(clubMember)
                .where(clubMember.club.id.eq(clubId))
                .execute();
    }
}
