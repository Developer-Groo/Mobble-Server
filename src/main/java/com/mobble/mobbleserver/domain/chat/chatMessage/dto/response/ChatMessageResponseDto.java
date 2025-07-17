package com.mobble.mobbleserver.domain.chat.chatMessage.dto.response;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.MessageType;

import java.time.LocalDateTime;

public record ChatMessageResponseDto(
        Long messageId,
        String content,
        MessageType type,
        Long senderId,
        String senderName,
        LocalDateTime sentAt
) {
}
