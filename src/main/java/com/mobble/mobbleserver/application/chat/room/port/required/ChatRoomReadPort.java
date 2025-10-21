package com.mobble.mobbleserver.application.chat.room.port.required;

import com.mobble.mobbleserver.domain.chat.room.ChatRoom;

import java.util.Optional;

public interface ChatRoomReadPort {

    Optional<ChatRoom> findChatRoomById(Long chatRoomId);

    boolean existsClubRoomInfo(Long clubId);
}
