package com.mobble.mobbleserver.application.chat.room.port.required;

public interface ChatRoomWritePort {

    boolean existsClubRoomInfoByClubId(Long clubId);
}
