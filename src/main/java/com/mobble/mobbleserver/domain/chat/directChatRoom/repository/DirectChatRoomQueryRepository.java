package com.mobble.mobbleserver.domain.chat.directChatRoom.repository;

public interface DirectChatRoomQueryRepository {

    boolean existsDirectChatRoomByBetweenMembers(Long senderId, Long receiverId);
}
