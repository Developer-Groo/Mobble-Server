package com.mobble.mobbleserver.infrastructure.persistence.chat.club;

import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRoomInfoRepository extends JpaRepository<ClubRoomInfo, Long> {

    boolean existsClubRoomInfoByClubId(Long clubId);
}
