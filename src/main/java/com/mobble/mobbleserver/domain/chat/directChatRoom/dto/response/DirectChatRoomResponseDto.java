package com.mobble.mobbleserver.domain.chat.directChatRoom.dto.response;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.MessageType;

import java.time.LocalDateTime;

public record DirectChatRoomResponseDto(
        Long chatRoomId,
        String content,
        MessageType type,
        Long senderId,
        String senderName,
        LocalDateTime sentAt
) {

    public static DirectChatRoomResponseDto toDto(
            Long chatRoomId,
            String content,
            MessageType type,
            Long senderId,
            String senderName,
            LocalDateTime sentAt
    ) {
        return new DirectChatRoomResponseDto(
                chatRoomId,
                content,
                type,
                senderId,
                senderName,
                sentAt
        );
    }
}

