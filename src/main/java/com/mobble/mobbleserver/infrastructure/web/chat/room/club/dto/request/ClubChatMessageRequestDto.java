package com.mobble.mobbleserver.infrastructure.web.chat.room.club.dto.request;

import com.mobble.mobbleserver.domain.chat.message.MessageType;

public record ClubChatMessageRequestDto(
        Long chatRoomId,
        String content,
        MessageType type
) {
}
