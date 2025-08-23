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
        int unreadCount
) {

    public static DirectChatRoomPreviewResponseDto toDto(DirectChatRoom directChatRoom, ChatMessage lastMessage, int unreadCount) {
        return new DirectChatRoomPreviewResponseDto(
                directChatRoom.getChatRoom().getId(),
                directChatRoom.getMemberB().getName(),
                directChatRoom.getMemberB().getProfileImage(),
                lastMessage.getContent(),
                lastMessage.getCreatedAt(),
                unreadCount
        );
    }
}
