package com.mobble.mobbleserver.application.chat.room.port.provided.command.club;

public interface ClubChatRoomJoinPort {

    void join(Long clubId, Long memberId);
}
