package com.mobble.mobbleserver.infrastructure.persistence.chat.message;

import com.mobble.mobbleserver.application.chat.message.port.required.MessageReadPort;
import com.mobble.mobbleserver.application.chat.message.port.required.MessageWritePort;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ChatMessagePersistenceAdapter implements MessageWritePort, MessageReadPort {

    private final JpaChatMessageRepository repository;

    /* MessageWritePort */
    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return repository.save(chatMessage);
    }

    @Override
    public void delete(Long chatRoomId) {
        repository.deleteChatMessagesByChatRoomId(chatRoomId);
    }

    /* MessageReadPort */
    @Override
    public List<ChatMessage> findMessagesFrom(Long chatRoomId, LocalDateTime startDate, Long lastMessageId, LocalDateTime lastCreatedAt) {
        return repository.findMessagesFrom(chatRoomId, startDate, lastMessageId, lastCreatedAt);
    }

    @Override
    public Map<Long, ChatMessage> findLatestMessagesByChatRoomIds(List<Long> chatRoomIds) {
        return repository.findLatestMessagesByChatRoomIds(chatRoomIds);
    }

    @Override
    public Map<Long, Integer> countUnreadMessagesByChatRoomIds(List<Long> chatRoomIds, Map<Long, Long> lastReadMessageIdsByChatRoom) {
        return repository.countUnreadMessagesByChatRoomIds(chatRoomIds, lastReadMessageIdsByChatRoom);
    }
}
