package com.mobble.mobbleserver.infrastructure.persistence.chat.message;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ChatMessageQueryRepository {

    List<ChatMessage> findMessagesFrom(Long chatroomId, LocalDateTime startDate, Long lastMessageId, LocalDateTime lastCreatedAt);

    Map<Long, ChatMessage> findLatestMessagesByChatRoomIds(List<Long> chatRoomIds);

    Map<Long, Integer> countUnreadMessagesByChatRoomIds(List<Long> chatRoomIds, Map<Long, Long> lastReadMessageIdsByChatRoom);
}
