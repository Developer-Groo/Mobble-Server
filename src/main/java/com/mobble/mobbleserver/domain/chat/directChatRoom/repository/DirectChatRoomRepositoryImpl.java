package com.mobble.mobbleserver.domain.chat.directChatRoom.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.chat.chatRoomParticipant.entity.QChatRoomParticipant.chatRoomParticipant;
import static com.mobble.mobbleserver.domain.chat.directChatRoom.entity.QDirectChatRoom.directChatRoom;

@RequiredArgsConstructor
public class DirectChatRoomRepositoryImpl implements DirectChatRoomQueryRepository{

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
                .selectFrom(directChatRoom)
                .where(
                        directChatRoom.chatRoom.id.in(chatRoomIds)
                )
                .fetchFirst() != null;
    }
}
