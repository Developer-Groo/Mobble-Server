package com.mobble.mobbleserver.refactor.chat.chatMessage.repository;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageQueryRepository {

    void deleteByChatRoomId(Long chatRoomId);
}
