package com.mobble.mobbleserver.infrastructure.persistence.clubMember;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.chat.room.QChatRoom.chatRoom;
import static com.mobble.mobbleserver.domain.chat.room.QClubRoomInfo.clubRoomInfo;
import static com.mobble.mobbleserver.domain.club.core.QClub.club;
import static com.mobble.mobbleserver.refactor.clubMember.entity.QClubMember.clubMember;

@RequiredArgsConstructor
public class ClubMemberQueryDslRepositoryImpl implements ClubMemberQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ClubMember> findAllClubMemberByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(clubMember)
                .join(clubMember.club, club)
                .fetchJoin()
                .join(club.clubRoomInfo, clubRoomInfo)
                .fetchJoin()
                .join(clubRoomInfo.chatRoom, chatRoom)
                .fetchJoin()
                .where(clubMember.member.id.eq(memberId))
                .fetch();
    }
}
