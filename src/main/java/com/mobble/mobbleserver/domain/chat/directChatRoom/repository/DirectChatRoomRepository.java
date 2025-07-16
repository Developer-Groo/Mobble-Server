package com.mobble.mobbleserver.domain.chat.directChatRoom.repository;

import com.mobble.mobbleserver.domain.chat.directChatRoom.entity.DirectChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectChatRoomRepository extends JpaRepository<DirectChatRoom, Long> {
}
