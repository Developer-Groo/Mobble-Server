package com.mobble.mobbleserver.application.chat.room.port.required;

import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;
import com.mobble.mobbleserver.domain.chat.room.Participant;

import java.util.List;
import java.util.Optional;

public interface ChatRoomReadPort {
    /* Common */
    // Todo: fetch join 고려
    Optional<ChatRoom> findChatRoomById(Long chatRoomId);

    /* Participant */
    List<Participant> findAllByChatRoomIdsAndMemberId(List<Long> chatRoomId, Long memberId);

    /* Club */
    Optional<ClubRoomInfo> findClubRoomInfoByClubId(Long clubId);

    List<ChatRoom> findChatRoomsByClubIds(List<Long> clubIds);

    boolean existsClubRoomInfo(Long clubId);

    /* Direct */
    List<DirectRoomInfo> findDirectChatRoomsAllByMemberId(Long memberId);

    boolean existsDirectChatRoomByBetweenMembers(Long senderId, Long receiverId);
}
