package com.mobble.mobbleserver.domain.chat.directChatRoom.dto.response;

import java.time.LocalDateTime;

public record DirectChatRoomPreviewResponseDto(
        Long chatRoomId,
        String opponentName,
        String opponentProfileImageUrl,
        String lastMessage,
        LocalDateTime lastMessageTime,
        int unreadCount
) {
}
