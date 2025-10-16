package com.mobble.mobbleserver.refactor.chat.directChatRoom.repository;

import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectChatRoomRepository extends JpaRepository<DirectRoomInfo, Long>, DirectChatRoomQueryRepository {
}
