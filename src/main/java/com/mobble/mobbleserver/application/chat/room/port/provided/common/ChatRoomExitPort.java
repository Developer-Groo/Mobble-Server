package com.mobble.mobbleserver.application.chat.room.port.provided.common;

public interface ChatRoomExitPort {

    void leave(Long chatRoomId, Long memberId);

    void delete(Long chatRoomId);
}
