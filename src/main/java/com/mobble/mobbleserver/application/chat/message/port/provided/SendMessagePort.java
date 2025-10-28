package com.mobble.mobbleserver.application.chat.message.port.provided;

import com.mobble.mobbleserver.infrastructure.web.chat.message.dto.request.ChatMessageRequestDto;

public interface SendMessagePort {

    void send(Long chatRoomId, Long senderId, ChatMessageRequestDto dto);
}
