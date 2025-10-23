package com.mobble.mobbleserver.infrastructure.web.chat.room.club.dto.response;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.club.core.Club;

import java.time.LocalDateTime;

public record ClubChatRoomPreviewResponseDto(
        Long chatRoomId,
        Long clubId,
        String clubName,
        String lastMessage,
        LocalDateTime lastMessageTime,
        int unreadCount,
        Long lastReadMessageId
) {

    public static ClubChatRoomPreviewResponseDto toDto(
            ChatRoom chatRoom,
            Club club,
            ChatMessage lastMessage,
            int unreadCount,
            Long lastReadMessageId
    ) {
        return new ClubChatRoomPreviewResponseDto(
                chatRoom.getId(),
                club.getId(),
                club.getName(),
                lastMessage != null ? lastMessage.getContent() : "",
                lastMessage != null ? lastMessage.getCreatedAt() : null,
                unreadCount,
                lastReadMessageId
        );
    }
}
