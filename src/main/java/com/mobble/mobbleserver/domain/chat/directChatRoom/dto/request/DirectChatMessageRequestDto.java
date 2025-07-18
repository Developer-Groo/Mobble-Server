package com.mobble.mobbleserver.domain.chat.directChatRoom.dto.request;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.MessageType;

public record DirectChatRoomRequestDto(
        Long chatRoomId,
        String content,
        MessageType type,
        Long receiverId
) {
}
