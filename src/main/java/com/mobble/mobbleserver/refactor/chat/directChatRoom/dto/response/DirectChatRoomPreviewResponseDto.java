package com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.response;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;

import java.time.LocalDateTime;

public record DirectChatRoomPreviewResponseDto(
        Long chatRoomId,
        String receiverName,
        String receiverProfileImageUrl,
        String lastMessage,
        LocalDateTime lastMessageTime,
        int unreadCount,
        Long lastReadMessageId
) {

    public static DirectChatRoomPreviewResponseDto toDto(
            DirectRoomInfo directRoomInfo,
            ChatMessage lastMessage,
            int unreadCount,
            Long lastReadMessageId
    ) {
        return new DirectChatRoomPreviewResponseDto(
                directRoomInfo.getChatRoom().getId(),
                directRoomInfo.getMemberB().getName(),
                directRoomInfo.getMemberB().getProfileImage(),
                lastMessage != null ? lastMessage.getContent() : "",
                lastMessage != null ? lastMessage.getCreatedAt() : null,
                unreadCount,
                lastReadMessageId
        );
    }
}
