package com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.request;

import com.mobble.mobbleserver.refactor.chat.chatMessage.entity.MessageType;

public record DirectChatMessageRequestDto(
        Long chatRoomId,
        String content,
        MessageType type,
        Long receiverId
) {
}
