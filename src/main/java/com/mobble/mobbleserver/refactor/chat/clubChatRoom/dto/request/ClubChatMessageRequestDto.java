package com.mobble.mobbleserver.refactor.chat.clubChatRoom.dto.request;

import com.mobble.mobbleserver.domain.chat.message.MessageType;

public record ClubChatMessageRequestDto(
        Long chatRoomId,
        String content,
        MessageType type
) {
}
