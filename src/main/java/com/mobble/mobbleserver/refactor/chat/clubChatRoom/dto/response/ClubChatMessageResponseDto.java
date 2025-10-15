package com.mobble.mobbleserver.refactor.chat.clubChatRoom.dto.response;

import com.mobble.mobbleserver.domain.chat.message.MessageType;

import java.time.LocalDateTime;

public record ClubChatMessageResponseDto(
        Long chatRoomId,
        String content,
        MessageType type,
        Long senderId,
        String senderName,
        LocalDateTime sentAt
) {

    public static ClubChatMessageResponseDto toDto(
            Long chatRoomId,
            String content,
            MessageType type,
            Long senderId,
            String senderName,
            LocalDateTime sentAt
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
