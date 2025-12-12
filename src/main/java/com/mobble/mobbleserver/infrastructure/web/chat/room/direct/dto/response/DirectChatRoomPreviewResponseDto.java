package com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.response;

import com.mobble.mobbleserver.application.chat.room.result.DirectChatRoomPreviewResult;

public record DirectChatRoomPreviewResponseDto(
        Long chatRoomId,
        Long receiverId,
        String receiverName,
        String receiverProfileImageUrl
) {

    public static DirectChatRoomPreviewResponseDto toDto(DirectChatRoomPreviewResult result) {
        return new DirectChatRoomPreviewResponseDto(
                result.chatRoomId(),
                result.receiverId(),
                result.receiverName(),
                result.receiverProfileImageUrl()
        );
    }
}
