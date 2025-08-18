package com.mobble.mobbleserver.domain.club.core.repository;

import com.mobble.mobbleserver.domain.club.core.repository.dto.ClubLikeInfoDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mobble.mobbleserver.domain.like.clubLike.entity.QClubLike.clubLike;

@Repository
@RequiredArgsConstructor
public class ClubRepositoryImpl implements ClubQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId) {
        Long likeCount = queryFactory
                .select(clubLike.count())
                .from(clubLike)
                .where(clubLike.club.id.eq(clubId))
                .fetchOne();

        Boolean isLiked = queryFactory
                .select(clubLike.isNotNull())
                .from(clubLike)
                .where(clubLike.club.id.eq(clubId), clubLike.member.id.eq(memberId))
                .fetchOne();

        return ClubLikeInfoDto.toDto(
                likeCount != null ? likeCount.intValue() : 0,
                isLiked != null && isLiked
        );
    }
}
