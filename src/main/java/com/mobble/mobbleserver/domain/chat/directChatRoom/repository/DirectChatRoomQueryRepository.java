package com.mobble.mobbleserver.domain.chat.directChatRoom.repository;

import com.mobble.mobbleserver.domain.chat.directChatRoom.entity.DirectChatRoom;

import java.util.List;

public interface DirectChatRoomQueryRepository {

    boolean existsDirectChatRoomByBetweenMembers(Long senderId, Long receiverId);

    List<DirectChatRoom> findDirectChatRoomsAllByMemberId(Long memberId);
}
