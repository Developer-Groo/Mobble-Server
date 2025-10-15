package com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.response;

import com.mobble.mobbleserver.domain.chat.message.MessageType;

import java.time.LocalDateTime;

public record DirectChatMessageResponseDto(
        Long chatRoomId,
        String content,
        MessageType type,
        Long senderId,
        String senderName,
        LocalDateTime sentAt
) {

    public static DirectChatMessageResponseDto toDto(
            Long chatRoomId,
            String content,
            MessageType type,
            Long senderId,
            String senderName,
            LocalDateTime sentAt
    ) {
        return new DirectChatMessageResponseDto(
                chatRoomId,
                content,
                type,
                senderId,
                senderName,
                sentAt
        );
    }
}

