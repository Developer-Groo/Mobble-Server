package com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DirectChatRoomCreateRequestDto(
        @NotNull(message = "receiverId must not be null")
        @Positive(message = "receiverId must be a positive number")
        Long receiverId
) {
}
