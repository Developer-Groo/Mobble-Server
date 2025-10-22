package com.mobble.mobbleserver.infrastructure.persistence.chat.room.direct;

import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectRoomInfoRepository extends JpaRepository<DirectRoomInfo, Long>, DirectRoomInfoQueryDslRepository {
}
