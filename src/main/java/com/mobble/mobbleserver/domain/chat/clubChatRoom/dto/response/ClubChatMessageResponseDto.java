package com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.MessageType;

public record ClubChatMessageResponseDto(
        Long chatRoomId,
        String content,
        MessageType type,
        Long senderId,
        String senderName,
        String sentAt
) {

    public static ClubChatMessageResponseDto toDto(
            Long chatRoomId,
            String content,
            MessageType type,
            Long senderId,
            String senderName,
            String sentAt
    ) {
        return new ClubChatMessageResponseDto(
                chatRoomId,
                content,
                type,
                senderId,
                senderName,
                sentAt
        );
    }
}
