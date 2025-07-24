package com.mobble.mobbleserver.domain.chat.chatMessage.repository;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;

import java.util.List;
import java.util.Map;

public interface ChatMessageQueryRepository {

    Map<Long, ChatMessage> findLatestMessagesByChatRoomIds(List<Long> chatRoomIds);

    Map<Long, Integer> countUnreadMessagesByChatRoomIds(List<Long> chatRoomIds, Map<Long, Long> lastReadMessageIdsByChatRoom);
}
