package com.mobble.mobbleserver.support.fixture.chat.chatRoom;

import com.mobble.mobbleserver.domain.chat.chatRoom.entity.ChatRoom;
import com.mobble.mobbleserver.domain.chat.chatRoom.entity.ChatRoomType;

public final class ChatRoomTestFixture {
    private ChatRoomTestFixture() {}

    public static ChatRoom createDefaultChatRoom() {
        return ChatRoom.createChatRoom(ChatRoomType.GROUP);
    }
}
