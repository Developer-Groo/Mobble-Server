package com.mobble.mobbleserver.refactor.chat.directChatRoom.repository;

import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;

import java.util.List;

public interface DirectChatRoomQueryRepository {

    boolean existsDirectChatRoomByBetweenMembers(Long senderId, Long receiverId);

    List<DirectRoomInfo> findDirectChatRoomsAllByMemberId(Long memberId);
}
