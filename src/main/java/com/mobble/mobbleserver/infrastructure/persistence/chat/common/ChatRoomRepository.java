package com.mobble.mobbleserver.infrastructure.persistence.chat.common;

import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
