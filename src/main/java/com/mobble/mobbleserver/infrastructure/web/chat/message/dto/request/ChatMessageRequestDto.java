package com.mobble.mobbleserver.infrastructure.web.chat.message.dto.request;

import com.mobble.mobbleserver.domain.chat.message.MessageType;

public record ChatMessageRequestDto(String content, MessageType type) {
}
