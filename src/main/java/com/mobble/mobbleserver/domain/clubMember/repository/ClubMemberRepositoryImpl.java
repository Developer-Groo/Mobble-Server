package com.mobble.mobbleserver.domain.clubMember.repository;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.chat.chatRoom.entity.QChatRoom.chatRoom;
import static com.mobble.mobbleserver.domain.chat.clubChatRoom.entity.QClubChatRoom.clubChatRoom;
import static com.mobble.mobbleserver.domain.club.club.entity.QClub.club;
import static com.mobble.mobbleserver.domain.clubMember.entity.QClubMember.clubMember;

@RequiredArgsConstructor
public class ClubMemberRepositoryImpl implements ClubMemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ClubMember> findAllClubMemberByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(clubMember)
                .join(clubMember.club, club)
                .fetchJoin()
                .join(club, clubChatRoom.club)
                .fetchJoin()
                .join(clubChatRoom.chatRoom, chatRoom)
                .fetchJoin()
                .where(clubMember.member.id.eq(memberId))
                .fetch();
    }
}
