package com.mobble.mobbleserver.infrastructure.web.chat.message.dto.request;

import com.mobble.mobbleserver.domain.chat.message.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageRequestDto(
        @NotBlank(message = "content must not be blank")
        String content,

        @NotNull(message = "type must not be null")
        MessageType type
) {
}
