package com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.request;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.MessageType;

public record ClubChatRoomRequestDto(
        Long chatRoomId,
        String content,
        MessageType type
) {
}
