package com.mobble.mobbleserver.infrastructure.persistence.chat.direct;

import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectChatRoomRepository extends JpaRepository<DirectRoomInfo, Long>, DirectChatRoomQueryRepository {
}
