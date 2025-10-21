package com.mobble.mobbleserver.infrastructure.persistence.chat.common;

import com.mobble.mobbleserver.domain.chat.room.Participant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.chat.message.QChatMessage.chatMessage;
import static com.mobble.mobbleserver.domain.chat.room.QParticipant.participant;

@RequiredArgsConstructor
public class ChatRoomParticipantRepositoryImpl implements ChatRoomParticipantQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Participant> findAllByChatRoomIdsAndMemberId(List<Long> chatRoomId, Long memberId) {
        return queryFactory
                .selectFrom(participant)
                .join(participant.chatRoom)
                .fetchJoin()
                .leftJoin(chatMessage)
                .on(chatMessage.id.eq(participant.lastReadMessageId))
                .fetchJoin()
                .where(
                        participant.chatRoom.id.in(chatRoomId),
                        participant.member.id.eq(memberId)
                )
                .fetch();
    }
}
