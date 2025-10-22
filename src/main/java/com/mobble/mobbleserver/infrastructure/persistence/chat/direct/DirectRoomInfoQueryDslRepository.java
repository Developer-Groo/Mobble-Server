package com.mobble.mobbleserver.infrastructure.persistence.chat.direct;

import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;

import java.util.List;

public interface DirectRoomInfoQueryDslRepository {

    boolean existsDirectChatRoomByBetweenMembers(Long senderId, Long receiverId);

    // Todo: N+1 문제 해결 필요 Member 접근 시 발생 가능성 있음
    List<DirectRoomInfo> findDirectChatRoomsAllByMemberId(Long memberId);
}
