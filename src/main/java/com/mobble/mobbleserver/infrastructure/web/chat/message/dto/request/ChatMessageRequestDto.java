package com.mobble.mobbleserver.infrastructure.web.chat.message.dto.request;

import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record ChatMessageRequestDto(
        @Positive
        Long chatRoomId,

        @Positive
        Long lastMessageId,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // Todo: error message 적용 필요
        LocalDateTime lastCreatedAt
) {
}
