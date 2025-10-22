package com.mobble.mobbleserver.refactor.club.search.repository;

import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.club.search.dto.request.ClubSearchRequestDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mobble.mobbleserver.domain.like.clubLike.QClubLike.clubLike;
import static com.mobble.mobbleserver.refactor.adress.entity.QAddress.address;
import static com.mobble.mobbleserver.refactor.club.clubGround.entity.QClubGround.clubGround;
import static com.mobble.mobbleserver.refactor.club.core.entity.QClub.club;
import static com.mobble.mobbleserver.refactor.clubCategory.entity.QClubCategory.clubCategory;
import static com.mobble.mobbleserver.refactor.ground.entity.QGround.ground;

@Repository
@RequiredArgsConstructor
public class ClubSearchRepository {

    private final JPAQueryFactory queryFactory;

    public List<Club> searchClubs(ClubSearchRequestDto req) {
        var query = queryFactory
                .selectFrom(club)
                .leftJoin(club.address, address).fetchJoin()
                .leftJoin(club.clubCategory, clubCategory).fetchJoin()
                .leftJoin(clubGround).on(clubGround.club.eq(club))
                .leftJoin(clubGround.ground, ground)
                .leftJoin(clubLike).on(clubLike.club.eq(club));

        if (req.name() != null && !req.name().isBlank()) {
            query.where(club.name.containsIgnoreCase(req.name()));
        }
        if (req.category() != null) {
            query.where(clubCategory.name.eq(req.category()));
        }
        if (req.isAutoJoin() != null) {
            query.where(club.isAutoJoin.eq(req.isAutoJoin()));
        }
        if (req.groundCode() != null) {
            query.where(ground.code.eq(req.groundCode()));
        }
        if (req.latitude() != null && req.longitude() != null && req.distanceKm() != null) {
            query.where(
                    address.latitude.subtract(req.latitude())
                            .multiply(address.latitude.subtract(req.latitude()))
                            .add(address.longitude.subtract(req.longitude())
                                    .multiply(address.longitude.subtract(req.longitude())))
                            .loe(req.distanceKm())
            );
        }

        if (req.sort() != null) {
            switch (req.sort().toUpperCase()) {
                case "LATEST" -> query.orderBy(club.createdAt.desc());
                case "LIKE" -> query.groupBy(club.id).orderBy(clubLike.count().desc());
                case "MEMBER" -> query.orderBy(club.headCount.desc());
            }
        }

        return query.fetch();
    }
}
