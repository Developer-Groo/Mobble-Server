package com.mobble.mobbleserver.domain.chat.chatRoom.repository;

import com.mobble.mobbleserver.domain.chat.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
