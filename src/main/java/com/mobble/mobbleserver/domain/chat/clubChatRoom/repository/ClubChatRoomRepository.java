package com.mobble.mobbleserver.domain.chat.clubChatRoom.repository;

import com.mobble.mobbleserver.domain.chat.clubChatRoom.entity.ClubChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubChatRoomRepository extends JpaRepository<ClubChatRoom, Long> {

    boolean existsClubChatRoomByClubId(Long clubId);

    Optional<ClubChatRoom> findByClubId(Long clubId);
}
