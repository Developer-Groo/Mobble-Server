package com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.request;

import com.mobble.mobbleserver.domain.chat.message.MessageType;

public record DirectChatMessageRequestDto(
        Long chatRoomId,
        String content,
        MessageType type,
        Long receiverId
) {
}
