package com.mobble.mobbleserver.application.chat.room.port.provided.club;

public interface ClubChatRoomJoinPort {

    void joinClubChatRoom(Long clubId, Long memberId);
}
