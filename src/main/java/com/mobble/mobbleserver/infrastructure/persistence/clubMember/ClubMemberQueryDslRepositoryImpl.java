package com.mobble.mobbleserver.infrastructure.persistence.clubMember;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.club.core.QClub.club;
import static com.mobble.mobbleserver.domain.clubMember.QClubMember.clubMember;
import static com.mobble.mobbleserver.refactor.chat.chatRoom.entity.QChatRoom.chatRoom;
import static com.mobble.mobbleserver.refactor.chat.clubChatRoom.entity.QClubChatRoom.clubChatRoom;

@RequiredArgsConstructor
public class ClubMemberQueryDslRepositoryImpl implements ClubMemberQueryDslRepository {

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
