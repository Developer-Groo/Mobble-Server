package com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.response;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;

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
            ChatRoom chatRoom,
            Member receiver,
            ChatMessage lastMessage,
            int unreadCount,
            Long lastReadMessageId
    ) {
        return new DirectChatRoomPreviewResponseDto(
                chatRoom.getId(),
                receiver.getName(),
                receiver.getProfileImage().getUrl(),
                lastMessage != null ? lastMessage.getContent() : "",
                lastMessage != null ? lastMessage.getCreatedAt() : null,
                unreadCount,
                lastReadMessageId
        );
    }
}
