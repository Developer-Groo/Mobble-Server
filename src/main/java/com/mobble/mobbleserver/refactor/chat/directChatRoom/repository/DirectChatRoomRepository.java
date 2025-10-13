package com.mobble.mobbleserver.refactor.chat.directChatRoom.repository;

import com.mobble.mobbleserver.refactor.chat.directChatRoom.entity.DirectChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectChatRoomRepository extends JpaRepository<DirectChatRoom, Long>, DirectChatRoomQueryRepository {
}
