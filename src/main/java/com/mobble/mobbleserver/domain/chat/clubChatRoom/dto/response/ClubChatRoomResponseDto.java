package com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.MessageType;

import java.time.LocalDateTime;

public record ClubChatRoomResponseDto(
        Long chatRoomId,
        String content,
        MessageType type,
        Long senderId,
        String senderName,
        LocalDateTime sentAt
) {

    public static ClubChatRoomResponseDto toDto(
            Long chatRoomId,
            String content,
            MessageType type,
            Long senderId,
            String senderName,
            LocalDateTime sentAt
    ) {
        return new ClubChatRoomResponseDto(
                chatRoomId,
                content,
                type,
                senderId,
                senderName,
                sentAt
        );
    }
}
