package com.mobble.mobbleserver.domain.chat.clubChatRoom.repository;

import com.mobble.mobbleserver.domain.chat.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
