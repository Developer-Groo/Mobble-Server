package com.mobble.mobbleserver.infrastructure.persistence.chat.direct;

import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.chat.room.QDirectRoomInfo.directRoomInfo;
import static com.mobble.mobbleserver.domain.chat.room.QParticipant.participant;

@RequiredArgsConstructor
public class DirectRoomInfoQueryDslRepositoryImpl implements DirectRoomInfoQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsDirectChatRoomByBetweenMembers(Long senderId, Long receiverId) {
        List<Long> chatRoomIds = queryFactory
                .select(participant.chatRoom.id)
                .from(participant)
                .where(participant.member.id.in(senderId, receiverId))
                .groupBy(participant.chatRoom.id)
                .having(participant.member.countDistinct().eq(2L))
                .fetch();

        return queryFactory
                .selectFrom(directRoomInfo)
                .where(directRoomInfo.chatRoom.id.in(chatRoomIds))
                .fetchFirst() != null;
    }

    @Override
    public List<DirectRoomInfo> findDirectChatRoomsAllByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(directRoomInfo)
                .where(
                        directRoomInfo.memberA.id.eq(memberId)
                                .or(directRoomInfo.memberB.id.eq(memberId))
                )
                .fetch();
    }
}
