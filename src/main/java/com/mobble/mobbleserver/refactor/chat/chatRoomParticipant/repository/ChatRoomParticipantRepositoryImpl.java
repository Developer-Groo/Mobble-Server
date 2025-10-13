package com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository;

import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.entity.ChatRoomParticipant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.entity.QChatRoomParticipant.chatRoomParticipant;

@RequiredArgsConstructor
public class ChatRoomParticipantRepositoryImpl implements ChatRoomParticipantQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatRoomParticipant> findAllByChatRoomIdsAndMemberId(List<Long> chatRoomId, Long memberId) {
        return queryFactory
                .selectFrom(chatRoomParticipant)
                .join(chatRoomParticipant.chatRoom)
                .fetchJoin()
                .leftJoin(chatRoomParticipant.lastReadMessage)
                .fetchJoin()
                .where(
                        chatRoomParticipant.chatRoom.id.in(chatRoomId),
                        chatRoomParticipant.member.id.eq(memberId)
                )
                .fetch();
    }
}
