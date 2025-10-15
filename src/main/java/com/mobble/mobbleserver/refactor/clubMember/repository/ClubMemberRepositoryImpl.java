package com.mobble.mobbleserver.refactor.clubMember.repository;

import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.chat.room.QChatRoom.chatRoom;
import static com.mobble.mobbleserver.refactor.chat.clubChatRoom.entity.QClubChatRoom.clubChatRoom;
import static com.mobble.mobbleserver.refactor.club.core.entity.QClub.club;
import static com.mobble.mobbleserver.refactor.clubMember.entity.QClubMember.clubMember;

@RequiredArgsConstructor
public class ClubMemberRepositoryImpl implements ClubMemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ClubMember> findAllClubMemberByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(clubMember)
                .join(clubMember.club, club)
                .fetchJoin()
                .join(club.clubChatRoom, clubChatRoom)
                .fetchJoin()
                .join(clubChatRoom.chatRoom, chatRoom)
                .fetchJoin()
                .where(clubMember.member.id.eq(memberId))
                .fetch();
    }
}
