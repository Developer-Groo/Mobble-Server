package com.mobble.mobbleserver.application.chat.message.port.required;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MessageReadPort {

    Optional<ChatMessage> findMessageByMessageId(Long messageId);

    List<ChatMessage> findMessagesFrom(Long roomId, LocalDateTime startDate, Long lastMessageId, LocalDateTime lastCreatedAt);

    Map<Long, ChatMessage> findLatestMessagesByChatRoomIds(List<Long> chatRoomIds);

    Map<Long, Integer> countUnreadMessagesByChatRoomIds(List<Long> chatRoomIds, Map<Long, Long> lastReadMessageIdsByChatRoom);
}
