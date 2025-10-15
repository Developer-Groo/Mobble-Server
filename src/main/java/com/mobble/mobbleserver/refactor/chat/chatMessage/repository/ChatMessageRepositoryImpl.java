package com.mobble.mobbleserver.refactor.chat.chatMessage.repository;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.refactor.chat.chatMessage.entity.QChatMessage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mobble.mobbleserver.refactor.chat.chatMessage.entity.QChatMessage.chatMessage;

@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatMessage> findMessagesFrom(
            Long chatroomId,
            LocalDateTime startDate,
            Long lastMessageId,
            LocalDateTime lastCreatedAt
    ) {
        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(chatMessage.chatRoom.id.eq(chatroomId));
        conditions.and(chatMessage.createdAt.goe(startDate));

        if (lastMessageId != null && lastCreatedAt != null) {
            conditions.and(
                    chatMessage.createdAt.lt(lastCreatedAt)
                            .or(chatMessage.createdAt.eq(lastCreatedAt))
                            .and(chatMessage.id.lt(lastMessageId))
            );
        }

        return queryFactory
                .selectFrom(chatMessage)
                .where(conditions)
                .orderBy(
                        chatMessage.createdAt.desc(),
                        chatMessage.id.desc()
                )
                .limit(50)
                .fetch();
    }

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
        Map<Long, LocalDateTime> createdAtByMessageId = findCreatedAtByMessageIds(lastReadMessageIdsByChatRoom);
        Map<Long, LocalDateTime> createdAtMap = mapChatRoomToCreatedAt(lastReadMessageIdsByChatRoom, createdAtByMessageId);
        BooleanBuilder conditions = buildCreatedAtCondition(createdAtMap);

        List<Tuple> result = queryFactory
                .select(chatMessage.chatRoom.id, chatMessage.count())
                .from(chatMessage)
                .where(conditions)
                .groupBy(chatMessage.chatRoom.id)
                .fetch();

        return result.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(chatMessage.chatRoom.id),
                        tuple -> Objects.requireNonNull(tuple.get(chatMessage.count())).intValue()
                ));
    }

    private Map<Long, LocalDateTime> findCreatedAtByMessageIds(Map<Long, Long> lastReadMessageIdsByChatRoom) {
        NumberPath<Long> id = chatMessage.id;
        DateTimePath<LocalDateTime> createdAt = chatMessage.createdAt;

        return queryFactory
                .select(id, createdAt)
                .from(chatMessage)
                .where(chatMessage.id.in(lastReadMessageIdsByChatRoom.values()))
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(id),
                        tuple -> tuple.get(createdAt)
                ));
    }

    private Map<Long, LocalDateTime> mapChatRoomToCreatedAt(Map<Long, Long> lastReadMessageIdsByChatRoom, Map<Long, LocalDateTime> lastReadCreatedAtByChatRoom) {
        return lastReadMessageIdsByChatRoom.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> lastReadCreatedAtByChatRoom.getOrDefault(entry.getValue(), LocalDateTime.MIN)
                ));
    }

    private BooleanBuilder buildCreatedAtCondition(Map<Long, LocalDateTime> createdAtMap) {
        BooleanBuilder conditions = new BooleanBuilder();
        createdAtMap.forEach((chatRoomId, createdAt) -> conditions.or(
                chatMessage.chatRoom.id.eq(chatRoomId)
                        .and(chatMessage.createdAt.gt(createdAt))
        ));

        return conditions;
    }
}
