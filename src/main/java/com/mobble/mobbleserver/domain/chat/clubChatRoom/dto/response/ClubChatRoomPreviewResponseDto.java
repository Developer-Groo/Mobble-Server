package com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response;

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
}
