package com.mobble.mobbleserver.application.chat.room.port.provided.command.participant;

public interface ChatRoomExitPort {

    void leave(Long chatRoomId, Long memberId);

    void delete(Long chatRoomId);
}
