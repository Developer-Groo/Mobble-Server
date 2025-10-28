package com.mobble.mobbleserver.application.chat.room.port.required;

import com.mobble.mobbleserver.domain.chat.room.ChatRoom;

public interface ChatRoomWritePort {

    ChatRoom save(ChatRoom chatRoom);

    void delete(ChatRoom chatRoom);
}
