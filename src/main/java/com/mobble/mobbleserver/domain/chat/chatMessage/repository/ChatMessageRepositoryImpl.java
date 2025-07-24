package com.mobble.mobbleserver.domain.chat.chatMessage.repository;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.domain.chat.chatMessage.entity.QChatMessage;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mobble.mobbleserver.domain.chat.chatMessage.entity.QChatMessage.chatMessage;

@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, ChatMessage> findLatestMessagesByChatRoomIds(List<Long> chatRoomIds) {
        QChatMessage subChatMessage = new QChatMessage("subChatMessage");

        List<ChatMessage> messages = queryFactory
                .selectFrom(chatMessage)
                .where(
                        Expressions.list(chatMessage.chatRoom.id, chatMessage.createdAt)
                                .in(
                                        JPAExpressions
                                                .select(subChatMessage.chatRoom.id, subChatMessage.createdAt.max())
                                                .from(chatMessage)
                                                .where(subChatMessage.chatRoom.id.in(chatRoomIds))
                                                .groupBy(subChatMessage.chatRoom.id)
                                )
                )
                .fetch();

        return messages.stream()
                .collect(Collectors.toMap(message ->
                                message.getChatRoom().getId(),
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public Map<Long, Integer> countUnreadMessagesByChatRoomIds(List<Long> chatRoomIds, Map<Long, Long> lastReadMessageIdsByChatRoom) {
        return Map.of();
    }
}
