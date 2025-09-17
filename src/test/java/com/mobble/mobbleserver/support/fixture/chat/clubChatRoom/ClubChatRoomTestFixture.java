package com.mobble.mobbleserver.support.fixture.chat.clubChatRoom;

import com.mobble.mobbleserver.domain.chat.chatRoom.entity.ChatRoom;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.entity.ClubChatRoom;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.support.fixture.chat.chatRoom.ChatRoomTestFixture;

public final class ClubChatRoomTestFixture {
    private ClubChatRoomTestFixture() {}

    public static ClubChatRoom createDefaultClubChatRoom(Club club) {
        ChatRoom room = ChatRoomTestFixture.createDefaultChatRoom();
        return ClubChatRoom.createClubChatRoom(club, room);
    }
}
