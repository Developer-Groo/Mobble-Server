package com.mobble.mobbleserver.refactor.chat.chatRoom.repository;

import com.mobble.mobbleserver.refactor.chat.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
