package com.mobble.mobbleserver.infrastructure.web.chat.room.query.dto;

import com.mobble.mobbleserver.application.chat.room.result.ChatRoomPreviewResult;

import java.time.LocalDateTime;
import java.util.List;

public record ChatRoomPreviewResponseDto(
        List<ChatRoomPreviewItemDto> rooms
) {

    public static ChatRoomPreviewResponseDto toDto(List<ChatRoomPreviewResult> results) {
        return new ChatRoomPreviewResponseDto(
                results.stream()
                        .map(ChatRoomPreviewItemDto::from)
                        .toList()
        );
    }

    public record ChatRoomPreviewItemDto(
            Long chatRoomId,
            String type,
            Long clubId,
            Long otherMemberId,
            String title,
            String imageUrl,
            String lastMessage,
            LocalDateTime lastMessageAt,
            int unreadCount,
            Long lastReadMessageId
    ) {

        public static ChatRoomPreviewItemDto from(ChatRoomPreviewResult result) {
            return new ChatRoomPreviewItemDto(
                    result.chatRoomId(),
                    result.roomType().name(),
                    result.clubId(),
                    result.otherMemberId(),
                    result.title(),
                    result.imageUrl(),
                    result.latestMessage(),
                    result.latestMessageAt(),
                    result.unreadCount(),
                    result.lastReadMessageId()
            );
        }
    }
}
