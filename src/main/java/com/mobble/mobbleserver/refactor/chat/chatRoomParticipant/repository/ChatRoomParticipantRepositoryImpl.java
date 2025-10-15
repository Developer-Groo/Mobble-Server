package com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository;

import com.mobble.mobbleserver.domain.chat.room.ChatRoomParticipant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.chat.room.QChatRoomParticipant.chatRoomParticipant;
import static com.mobble.mobbleserver.refactor.chat.chatMessage.entity.QChatMessage.chatMessage;

@RequiredArgsConstructor
public class ChatRoomParticipantRepositoryImpl implements ChatRoomParticipantQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatRoomParticipant> findAllByChatRoomIdsAndMemberId(List<Long> chatRoomId, Long memberId) {
        return queryFactory
                .selectFrom(chatRoomParticipant)
                .join(chatRoomParticipant.chatRoom)
                .fetchJoin()
                .leftJoin(chatMessage)
                .on(chatMessage.id.eq(chatRoomParticipant.lastReadMessageId))
                .fetchJoin()
                .where(
                        chatRoomParticipant.chatRoom.id.in(chatRoomId),
                        chatRoomParticipant.member.id.eq(memberId)
                )
                .fetch();
    }
}
