package com.mobble.mobbleserver.domain.chat.directChatRoom.repository;

import com.mobble.mobbleserver.domain.chat.directChatRoom.entity.DirectChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectChatRoomRepository extends JpaRepository<DirectChatRoom, Long>, DirectChatRoomQueryRepository {
}
