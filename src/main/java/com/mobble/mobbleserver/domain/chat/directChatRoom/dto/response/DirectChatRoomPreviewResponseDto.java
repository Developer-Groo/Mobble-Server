package com.mobble.mobbleserver.domain.chat.directChatRoom.dto.response;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.domain.chat.directChatRoom.entity.DirectChatRoom;

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
            DirectChatRoom directChatRoom,
            ChatMessage lastMessage,
            int unreadCount,
            Long lastReadMessageId
    ) {
        return new DirectChatRoomPreviewResponseDto(
                directChatRoom.getChatRoom().getId(),
                directChatRoom.getMemberB().getName(),
                directChatRoom.getMemberB().getProfileImage(),
                lastMessage != null ? lastMessage.getContent() : "",
                lastMessage != null ? lastMessage.getCreatedAt() : null,
                unreadCount,
                lastReadMessageId
        );
    }
}
