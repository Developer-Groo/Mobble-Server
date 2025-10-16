package com.mobble.mobbleserver.support.fixture.chat.clubChatRoom;

import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.support.fixture.chat.chatRoom.ChatRoomTestFixture;

public final class ClubChatRoomTestFixture {
    private ClubChatRoomTestFixture() {}

    public static ClubRoomInfo createDefaultClubChatRoom(Club club) {
        ChatRoom room = ChatRoomTestFixture.createDefaultChatRoom();
        return ClubRoomInfo.createClubChatRoom(club, room);
    }
}
