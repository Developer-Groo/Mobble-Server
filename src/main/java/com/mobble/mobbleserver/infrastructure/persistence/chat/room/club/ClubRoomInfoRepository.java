package com.mobble.mobbleserver.infrastructure.persistence.chat.room.club;

import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRoomInfoRepository extends JpaRepository<ClubRoomInfo, Long> {

    Optional<ClubRoomInfo> findByClub_Id(Long clubId);

    boolean existsByClub_Id(Long clubId);
}
