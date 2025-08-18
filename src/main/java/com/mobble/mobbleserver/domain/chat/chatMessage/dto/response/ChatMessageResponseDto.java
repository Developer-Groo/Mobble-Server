package com.mobble.mobbleserver.domain.chat.chatMessage.dto.response;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.domain.chat.chatMessage.entity.MessageType;
import com.mobble.mobbleserver.util.DateTimeUtils;

import java.time.LocalDateTime;

public record ChatMessageResponseDto(
        Long messageId,
        String content,
        MessageType type,
        Long senderId,
        String senderName,
        LocalDateTime sentAt
) {

    public static ChatMessageResponseDto toDto(ChatMessage chatMessage) {
        return new ChatMessageResponseDto(
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getType(),
                chatMessage.getSender().getId(),
                chatMessage.getSender().getName(),
                DateTimeUtils.toKST(chatMessage.getCreatedAt())
        );
    }
}
