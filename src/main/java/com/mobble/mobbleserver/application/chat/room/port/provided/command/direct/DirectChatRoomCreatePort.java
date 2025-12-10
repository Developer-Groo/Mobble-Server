package com.mobble.mobbleserver.application.chat.room.port.provided.command.direct;

import com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.request.DirectChatRoomCreateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.chat.room.direct.dto.response.DirectChatRoomPreviewResponseDto;

public interface DirectChatRoomCreatePort {

    DirectChatRoomPreviewResponseDto create(DirectChatRoomCreateRequestDto dto, Long memberId);
}
