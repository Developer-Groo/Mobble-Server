package com.mobble.mobbleserver.application.chat.message.port.provided;

import com.mobble.mobbleserver.infrastructure.web.chat.message.dto.response.ChatMessageResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageQueryPort {

    List<ChatMessageResponseDto> list(Long chatRoomId, Long memberId, Long cursor, LocalDateTime lastCreatedAt, String direction, int limit);
}
