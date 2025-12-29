package com.mobble.mobbleserver.infrastructure.web.chat.message.dto.response;

import com.mobble.mobbleserver.application.chat.message.result.ChatMessageResult;
import com.mobble.mobbleserver.application.chat.message.result.ChatMessageSliceResult;
import com.mobble.mobbleserver.domain.chat.message.MessageType;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageResponseDto(
        boolean hasMore,
        Long nextCursorId,
        LocalDateTime nextCursorCreatedAt,
        List<ChatMessageItemDto> items
) {

    public static ChatMessageResponseDto toDto(ChatMessageSliceResult result) {
        return new ChatMessageResponseDto(
                result.hasMore(),
                result.nextCursorId(),
                result.nextCursorCreatedAt(),
                result.results().stream()
                        .map(ChatMessageItemDto::from)
                        .toList()
        );
    }

    public record ChatMessageItemDto(
            Long chatRoomId,
            Long messageId,
            String content,
            MessageType type,
            Long senderId,
            String senderName,
            String senderProfileImageUrl,
            LocalDateTime sentAt
    ) {

        public static ChatMessageItemDto from(ChatMessageResult result) {
            return new ChatMessageItemDto(
                    result.chatRoomId(),
                    result.messageId(),
                    result.content(),
                    result.type(),
                    result.senderId(),
                    result.senderName(),
                    result.senderProfileImageUrl(),
                    result.sentAt()
            );
        }
    }
}
