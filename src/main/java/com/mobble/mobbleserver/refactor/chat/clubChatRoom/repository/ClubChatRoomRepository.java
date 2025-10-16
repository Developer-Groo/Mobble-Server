package com.mobble.mobbleserver.refactor.chat.clubChatRoom.repository;

import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubChatRoomRepository extends JpaRepository<ClubRoomInfo, Long> {

    boolean existsClubChatRoomByClubId(Long clubId);

    Optional<ClubRoomInfo> findByClubId(Long clubId);
}
