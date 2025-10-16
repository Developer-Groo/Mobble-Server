package com.mobble.mobbleserver.refactor.chat.directChatRoom.repository;

import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.chat.room.QChatRoomParticipant.chatRoomParticipant;
import static com.mobble.mobbleserver.domain.chat.room.QDirectRoomInfo.directRoomInfo;

@RequiredArgsConstructor
public class DirectChatRoomRepositoryImpl implements DirectChatRoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsDirectChatRoomByBetweenMembers(Long senderId, Long receiverId) {
        List<Long> chatRoomIds = queryFactory
                .select(chatRoomParticipant.chatRoom.id)
                .from(chatRoomParticipant)
                .where(chatRoomParticipant.member.id.in(senderId, receiverId))
                .groupBy(chatRoomParticipant.chatRoom.id)
                .having(chatRoomParticipant.member.countDistinct().eq(2L))
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
