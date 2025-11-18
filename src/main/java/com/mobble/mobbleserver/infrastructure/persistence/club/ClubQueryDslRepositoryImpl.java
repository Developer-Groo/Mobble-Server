package com.mobble.mobbleserver.infrastructure.persistence.club;

import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.infrastructure.persistence.club.projection.ClubLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubSearchRequestDto;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.like.QClubLike.clubLike;

@RequiredArgsConstructor
public class ClubQueryDslRepositoryImpl implements ClubQueryDslRepository{

    private final JPQLQueryFactory queryFactory;

    @Override
    public ClubLikeInfoDto findLikeInfoByClubIdAndMemberId(Long clubId, Long memberId) {
        Long likeCount = queryFactory
                .select(clubLike.count())
                .from(clubLike)
                .where(clubLike.clubId.eq(clubId))
                .fetchOne();

        Boolean isLiked = queryFactory
                .select(clubLike.isNotNull())
                .from(clubLike)
                .where(clubLike.clubId.eq(clubId), clubLike.memberId.eq(memberId))
                .fetchOne();

        return ClubLikeInfoDto.toDto(
                likeCount != null ? likeCount.intValue() : 0,
                isLiked != null && isLiked
        );
    }

    @Override
    public List<Club> searchClubs(ClubSearchRequestDto req) {
//        var query = queryFactory
//                .selectFrom(club)
//                .leftJoin(club.address, address).fetchJoin()
//                .leftJoin(club.clubCategory, clubCategory).fetchJoin()
//                .leftJoin(clubGround).on(clubGround.club.eq(club))
//                .leftJoin(clubGround.ground, ground)
//                .leftJoin(clubLike).on(clubLike.clubId.eq(club.id));
//
//        if (req.name() != null && !req.name().isBlank()) {
//            query.where(club.name.containsIgnoreCase(req.name()));
//        }
//        if (req.category() != null) {
//            query.where(clubCategory.name.eq(req.category()));
//        }
//        if (req.isAutoJoin() != null) {
//            query.where(club.isAutoJoin.eq(req.isAutoJoin()));
//        }
//        if (req.groundCode() != null) {
//            query.where(ground.code.eq(req.groundCode()));
//        }
//        if (req.latitude() != null && req.longitude() != null && req.distanceKm() != null) {
//            query.where(
//                    address.latitude.subtract(req.latitude())
//                            .multiply(address.latitude.subtract(req.latitude()))
//                            .add(address.longitude.subtract(req.longitude())
//                                    .multiply(address.longitude.subtract(req.longitude())))
//                            .loe(req.distanceKm())
//            );
//        }
//
//        if (req.sort() != null) {
//            switch (req.sort().toUpperCase()) {
//                case "LATEST" -> query.orderBy(club.createdAt.desc());
//                case "LIKE" -> query.groupBy(club.id).orderBy(clubLike.count().desc());
//                case "MEMBER" -> query.orderBy(club.headCount.desc());
//            }
//        }
//
//        return query.fetch();
        return null;
    }
}
