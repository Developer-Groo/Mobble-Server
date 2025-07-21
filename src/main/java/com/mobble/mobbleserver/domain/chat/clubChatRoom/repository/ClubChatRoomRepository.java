package com.mobble.mobbleserver.domain.chat.clubChatRoom.repository;

import com.mobble.mobbleserver.domain.chat.clubChatRoom.entity.ClubChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubChatRoomRepository extends JpaRepository<ClubChatRoom, Long> {
}
