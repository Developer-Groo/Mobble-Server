package com.mobble.mobbleserver.application.chat.room.port.provided.club;

public interface ClubChatRoomCreatePort {

    void createClubChatRoom(Long clubId, Long memberId);
}
