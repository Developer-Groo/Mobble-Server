package com.mobble.mobbleserver.application.chat.room.port.provided.command.club;

public interface ClubChatRoomCreatePort {

    void create(Long clubId, Long memberId);
}
