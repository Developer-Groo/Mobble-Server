package com.mobble.mobbleserver.application.chat.room.port.provided.command.direct;

import com.mobble.mobbleserver.application.chat.room.result.DirectChatRoomPreviewResult;

public interface DirectChatRoomCreatePort {

    DirectChatRoomPreviewResult create(Long receiverId, Long memberId);
}
