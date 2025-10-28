package com.mobble.mobbleserver.infrastructure.persistence.clubMember;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ClubMemberQueryDslRepositoryImpl implements ClubMemberQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ClubMember> findAllClubMemberByMemberId(Long memberId) {
        return List.of();
        // Todo: 로직 수정 필요
//                queryFactory
//                .selectFrom(clubMember)
//                .join(clubMember.club, club)
//                .fetchJoin()
//                .join(club.clubRoomInfo, clubRoomInfo)
//                .fetchJoin()
//                .join(clubRoomInfo.chatRoom, chatRoom)
//                .fetchJoin()
//                .where(clubMember.member.id.eq(memberId))
//                .fetch();
    }
}
